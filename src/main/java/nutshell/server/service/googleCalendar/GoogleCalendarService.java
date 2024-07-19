package nutshell.server.service.googleCalendar;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nutshell.server.constant.GoogleConstant;
import nutshell.server.domain.GoogleCalendar;
import nutshell.server.domain.GoogleSchedule;
import nutshell.server.domain.User;
import nutshell.server.dto.googleCalender.response.*;
import nutshell.server.exception.BusinessException;
import nutshell.server.exception.code.BusinessErrorCode;
import nutshell.server.feign.google.GoogleReissueRequest;
import nutshell.server.feign.google.GoogleTokenResponse;
import nutshell.server.feign.google.GoogleUserInfoResponse;
import nutshell.server.service.google.GoogleService;
import nutshell.server.service.googleCategory.GoogleCategoryRetriever;
import nutshell.server.service.googleSchedule.GoogleScheduleRetriever;
import nutshell.server.service.googleSchedule.GoogleScheduleService;
import nutshell.server.service.user.UserRetriever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleCalendarService {
    private final UserRetriever userRetriever;
    private final GoogleCalendarRetriever googleCalendarRetriever;
    private final GoogleCalendarUpdater googleCalendarUpdater;
    private final GoogleCalendarRemover googleCalendarRemover;
    private final GoogleService googleService;
    private final GoogleCalendarSaver googleCalendarSaver;
    private final GoogleScheduleRetriever googleScheduleRetriever;
    private final GoogleCategoryRetriever googleCategoryRetriever;
    private final GoogleScheduleService googleScheduleService;
    @Value("${google.calender.client-id}")
    private String CLIENT_ID;
    @Value("${google.calender.client-secret}")
    private String CLIENT_SECRET;
    @Value("${google.calender.redirect-uri}")
    private String REDIRECT_URI;

    @Transactional
    public GoogleCalendar register(final String code, final Long userId) {
        User user = userRetriever.findByUserId(userId);
        GoogleTokenResponse tokens = googleService.getToken(
                code,
                CLIENT_ID,
                CLIENT_SECRET,
                REDIRECT_URI
        );
        assert tokens != null;
        GoogleUserInfoResponse data = googleService.getUserInfo(tokens.accessToken());
        assert data != null;
        if(googleCalendarRetriever.existsByUserAndEmail(user, data.email())){
            throw new BusinessException(BusinessErrorCode.GOOGLE_SERVER_EXIST);
        }
        return googleCalendarSaver.save(
                GoogleCalendar.builder()
                        .user(user)
                        .email(data.email())
                        .serialId(data.sub())
                        .accessToken(tokens.accessToken())
                        .refreshToken(tokens.refreshToken())
                        .build()
        );
    }

    @Transactional
    public void unlink(final Long userId, final Long googleCalenderId){
        User user = userRetriever.findByUserId(userId);
        GoogleCalendar googleCalendar = googleCalendarRetriever.findByIdAndUser(googleCalenderId, user);
        try {
            googleService.unlink(googleCalendar.getAccessToken());
        } catch (Exception e) {
            reissue(googleCalendar);
            googleService.unlink(googleCalendar.getAccessToken());
        }
        googleCalendarRemover.remove(googleCalendar);
    }

    @Async
    public void getSyncs(final Long userId) {
        User user = userRetriever.findByUserId(userId);
        googleCalendarRetriever.findAllByUser(user)
                .forEach(
                        googleCalendar -> {
                            try {
                                googleScheduleService.syncCalendar(googleCalendar);
                            } catch (Exception e) {
                                reissue(googleCalendar);
                                try {
                                    googleScheduleService.syncCalendar(googleCalendar);
                                } catch (Exception ex) {
                                    log.error("Google Calender Error : {}", ex.getMessage());
                                }
                            }
                        }
                );
    }

    @Transactional
    public List<GoogleSchedulesDto> getGoogleCalendars(
            final Long userId,
            final LocalDate startDate,
            final Integer range,
            final List<String> categories
    ) {
        User user = userRetriever.findByUserId(userId);
        List<GoogleCalendar> googleCalendars = googleCalendarRetriever.findAllByUser(user);
        List<GoogleSchedule> schedules = new ArrayList<>();
        googleCalendars.forEach(
                googleCalender -> schedules.addAll(getEvents(googleCalender, startDate, range, categories))
        );
        return schedules.stream().map(
                googleSchedule -> GoogleSchedulesDto.builder()
                        .id(googleSchedule.getId().split(":")[1])
                        .name(googleSchedule.getName())
                        .color(googleSchedule.getColor())
                        .schedules(googleSchedule.getSchedules())
                        .build()
        ).toList();
    }

    public GoogleEmailsDto getCategories(
            final Long userId
    ) {
        User user = userRetriever.findByUserId(userId);
        List<GoogleEmailsDto.GoogleEmailDto> emails = new ArrayList<>();
        googleCalendarRetriever.findAllByUser(user)
                .forEach(
                        googleCalender -> {
                            List<GoogleEmailsDto.GoogleEmailDto.GoogleCategoryDto> categories = getCategories(googleCalender);
                            emails.add(GoogleEmailsDto.GoogleEmailDto.builder()
                                    .email(googleCalender.getEmail())
                                    .categories(categories)
                                    .build());
                        }
                );
        return GoogleEmailsDto.builder()
                .emails(emails)
                .build();
    }

    private List<GoogleEmailsDto.GoogleEmailDto.GoogleCategoryDto> getCategories(
            final GoogleCalendar googleCalendar
    ) {
        return googleCategoryRetriever
                .findAllByGoogleCalendarId(googleCalendar.getId()).stream().map(
                googleCategory -> GoogleEmailsDto.GoogleEmailDto.GoogleCategoryDto.builder()
                        .id(googleCategory.getId().split(":")[1])
                        .name(googleCategory.getName())
                        .color(googleCategory.getColor())
                        .build()
        ).toList();
    }

    private List<GoogleSchedule> getEvents(
            final GoogleCalendar googleCalendar,
            final LocalDate startDate,
            final Integer range,
            final List<String> categories
    ) {
        List<GoogleSchedule> googleSchedules = new ArrayList<>();
        if (categories != null && !categories.isEmpty()) {
            categories.forEach(
                    category -> {
                        GoogleSchedule schedule = googleScheduleRetriever.findById(googleCalendar.getId(), category);
                        if (schedule != null)
                            googleSchedules.add(schedule);
                    }
            );
        } else {
            googleSchedules.addAll(googleScheduleRetriever.findAllByGoogleCalendarId(googleCalendar.getId()));
        }
        List<GoogleSchedule> schedules = new ArrayList<>();
        googleSchedules.forEach(
                googleSchedule -> {
                    List<GoogleSchedulesDto.GoogleScheduleDto> googleSchedulesDto = googleSchedule.getSchedules().stream().filter(
                            googleScheduleDto -> googleScheduleDto.startTime().toLocalDate().isAfter(startDate.minusDays(1))
                                    && googleScheduleDto.startTime().toLocalDate().isBefore(startDate.plusDays(range))
                    ).toList();
                    if (!googleSchedulesDto.isEmpty()) {
                        schedules.add(
                                GoogleSchedule.builder()
                                        .id(googleSchedule.getId())
                                        .name(googleSchedule.getName())
                                        .color(googleSchedule.getColor())
                                        .googleCalendarId(googleSchedule.getGoogleCalendarId())
                                        .googleCategoryId(googleSchedule.getGoogleCategoryId())
                                        .schedules(googleSchedulesDto)
                                        .build());
                    }
                }
        );
        return schedules;
    }

    private void reissue(final GoogleCalendar googleCalendar) {
        GoogleTokenResponse tokens = googleService.reissue(
                GoogleReissueRequest.builder()
                        .clientId(CLIENT_ID)
                        .clientSecret(CLIENT_SECRET)
                        .redirectUri(REDIRECT_URI)
                        .refreshToken(googleCalendar.getRefreshToken())
                        .grantType(GoogleConstant.REFRESH_TOKEN)
                        .build()
        );
        assert tokens != null;
        log.info("{}", tokens.accessToken());
        googleCalendarUpdater.updateTokens(googleCalendar, tokens.accessToken());
    }
}