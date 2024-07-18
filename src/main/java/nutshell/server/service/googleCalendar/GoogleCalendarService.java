package nutshell.server.service.googleCalendar;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nutshell.server.constant.GoogleConstant;
import nutshell.server.domain.GoogleCalendar;
import nutshell.server.domain.User;
import nutshell.server.dto.googleCalender.request.CategoriesDto;
import nutshell.server.dto.googleCalender.response.*;
import nutshell.server.exception.BusinessException;
import nutshell.server.exception.code.BusinessErrorCode;
import nutshell.server.feign.google.GoogleReissueRequest;
import nutshell.server.feign.google.GoogleTokenResponse;
import nutshell.server.feign.google.GoogleUserInfoResponse;
import nutshell.server.service.google.GoogleService;
import nutshell.server.service.user.UserRetriever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final NetHttpTransport HTTP_TRANSPORT;
    @Value("${google.calender.client-id}")
    private String CLIENT_ID;
    @Value("${google.calender.client-secret}")
    private String CLIENT_SECRET;
    @Value("${google.calender.redirect-uri}")
    private String REDIRECT_URI;
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }
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

    @Transactional
    public List<GoogleSchedulesDto> getGoogleCalendars(
            final Long userId,
            final LocalDate startDate,
            final Integer range,
            final CategoriesDto categoriesDto
    ) {
        User user = userRetriever.findByUserId(userId);
        List<GoogleCalendar> googleCalendars = googleCalendarRetriever.findAllByUser(user);
        List<GoogleSchedulesDto> schedules = new ArrayList<>();
        googleCalendars.forEach(
                googleCalender -> {
                    try {
                        schedules.addAll(getEvents(googleCalender, startDate, range, categoriesDto));
                    } catch (Exception e) {
                        reissue(googleCalender);
                        try {
                            schedules.addAll(getEvents(googleCalender, startDate, range, categoriesDto));
                        } catch (Exception ex) {
                            log.error("Google Calender Error : {}", ex.getMessage());
                        }
                    }
                }
        );

        return schedules;
    }

    public GoogleCategoriesDto getCategories(
            Long userId
    ) {
        User user = userRetriever.findByUserId(userId);
        List<GoogleCategoriesDto.GoogleCategoryDto> categories = new ArrayList<>();
        googleCalendarRetriever.findAllByUser(user)
                .forEach(
                        googleCalender -> {
                            try {
                                categories.addAll(getCategories(googleCalender));
                            } catch (Exception e) {
                                reissue(googleCalender);
                                try {
                                    categories.addAll(getCategories(googleCalender));
                                } catch (Exception ex) {
                                    log.error("Google Calender Error : {}", ex.getMessage());
                                }
                            }
                        }
                );
        return GoogleCategoriesDto.builder()
                .categories(categories)
                .build();
    }

    private Calendar getCalendar(
            final GoogleCalendar googleCalendar
    ) {
        Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setJsonFactory(JSON_FACTORY)
                .setTransport(HTTP_TRANSPORT)
                .build()
                .setAccessToken(googleCalendar.getAccessToken());
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(GoogleConstant.APPLICATION_NAME)
                .build();
    }

    private List<GoogleCategoriesDto.GoogleCategoryDto> getCategories(
            final GoogleCalendar googleCalendar
    ) throws IOException {
        Calendar calender = getCalendar(googleCalendar);
        CalendarList calendarList = calender.calendarList().list().execute();
        List<CalendarListEntry> items =  calendarList.getItems();
        List<GoogleCategoriesDto.GoogleCategoryDto> categories = new ArrayList<>();
        for (CalendarListEntry calendarListEntry : items) {
            String calendarId = calendarListEntry.getId();
            String calendars = calendarListEntry.getSummary();
            String color = calendarListEntry.getBackgroundColor();
            categories.add(GoogleCategoriesDto.GoogleCategoryDto.builder()
                    .id(calendarId)
                    .name(calendars)
                    .color(color)
                    .build()
            );
        }
        return categories;
    }

    private List<GoogleSchedulesDto> getEvents(
            final GoogleCalendar googleCalendar,
            final LocalDate startDate,
            final Integer range,
            final CategoriesDto categoriesDto
    ) throws IOException {
        List<GoogleSchedulesDto> schedules = new ArrayList<>();
        Calendar calender = getCalendar(googleCalendar);
        CalendarList calendarList = calender.calendarList().list().execute();
        List<CalendarListEntry> items =  calendarList.getItems();
        for (CalendarListEntry calendarListEntry : items) {
            String calendarId = calendarListEntry.getId();
            if (categoriesDto != null && categoriesDto.categories() != null && !categoriesDto.categories().contains(calendarId)) {
                continue;
            }
            String calendars = calendarListEntry.getSummary();
            String color = calendarListEntry.getBackgroundColor();
            DateTime startTime = new DateTime(startDate.atStartOfDay() + ":00Z");
            DateTime endTime = new DateTime(startDate.plusDays(range - 1).atTime(23, 59) + ":00Z");
            Events events = calender.events().list(calendarId).setTimeMin(startTime).setTimeMax(endTime).execute();
            List<GoogleSchedulesDto.GoogleScheduleDto> googleScheduleDtoList = new ArrayList<>();
            for (Event event : events.getItems()) {
                LocalDateTime start = getLocalDateTime(event.getStart());
                LocalDateTime end = getLocalDateTime(event.getEnd());
                GoogleSchedulesDto.GoogleScheduleDto googleScheduleDto = GoogleSchedulesDto.GoogleScheduleDto.builder()
                        .name(event.getSummary())
                        .startTime(start)
                        .endTime(end)
                        .allDay(!start.toLocalDate().equals(end.toLocalDate()))
                        .build();
                googleScheduleDtoList.add(googleScheduleDto);
            }
            if (googleScheduleDtoList.isEmpty()) {
                continue;
            }
            schedules.add(GoogleSchedulesDto.builder()
                    .id(calendarId)
                    .name(calendars)
                    .color(color)
                    .schedules(googleScheduleDtoList)
                    .build()
            );
        }
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

    private LocalDateTime getLocalDateTime(final EventDateTime event) {
        LocalDateTime time = null;
        if (event != null) {
            if (event.getDateTime() != null) {
                // dateTime 값이 있는 경우
                time = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(event.getDateTime().getValue()),
                        ZoneId.of("Asia/Seoul")
                ).plusMinutes(event.getDateTime().getTimeZoneShift());
            } else if (event.getDate() != null) {
                // date 값만 있는 경우
                time = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(event.getDate().getValue()),
                        ZoneId.of("Asia/Seoul")
                ).plusMinutes(event.getDate().getTimeZoneShift());
            }
        }
        return time;
    }
}