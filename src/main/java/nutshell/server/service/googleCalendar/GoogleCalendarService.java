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
import nutshell.server.constant.GoogleCalendarConstant;
import nutshell.server.domain.GoogleCalendar;
import nutshell.server.domain.User;
import nutshell.server.dto.googleCalender.request.CategoriesDto;
import nutshell.server.dto.googleCalender.response.GoogleScheduleDto;
import nutshell.server.dto.googleCalender.response.GoogleSchedulesDto;
import nutshell.server.dto.googleCalender.response.GoogleTokenDto;
import nutshell.server.dto.googleCalender.response.GoogleUserInfo;
import nutshell.server.exception.BusinessException;
import nutshell.server.exception.code.BusinessErrorCode;
import nutshell.server.service.user.UserRetriever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

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
    private final GoogleCalendarSaver googleCalendarSaver;
    private final GoogleCalendarRetriever googleCalendarRetriever;
    private final GoogleCalendarUpdater googleCalendarUpdater;
    private final GoogleCalendarRemover googleCalendarRemover;

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
    public GoogleCalendar getToken(final String code, final Long userId) {
        User user = userRetriever.findById(userId);
        RestClient restClient = RestClient.create();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(GoogleCalendarConstant.CLIENT_ID, CLIENT_ID);
        body.add(GoogleCalendarConstant.CLIENT_SECRET, CLIENT_SECRET);
        body.add(GoogleCalendarConstant.REDIRECT_URI, REDIRECT_URI);
        body.add(GoogleCalendarConstant.CODE, code);
        body.add(GoogleCalendarConstant.GRANT_TYPE, GoogleCalendarConstant.AUTHORIZATION_CODE);
        GoogleTokenDto tokens = restClient.post()
                .uri(GoogleCalendarConstant.GOOGLE_TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new BusinessException(BusinessErrorCode.GOOGLE_SERVER_ERROR);
                }).body(GoogleTokenDto.class);
        assert tokens != null;
        GoogleUserInfo data = restClient.get()
                .uri(GoogleCalendarConstant.GOOGLE_USER_INFO_URL)
                .header(GoogleCalendarConstant.AUTHORIZATION, GoogleCalendarConstant.BEARER + tokens.accessToken())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new BusinessException(BusinessErrorCode.GOOGLE_SERVER_ERROR);
                }).onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new BusinessException(BusinessErrorCode.GOOGLE_SERVER_ERROR);
                })
        .body(GoogleUserInfo.class);
        assert data != null;
        GoogleCalendar googleCalendar = GoogleCalendar.builder()
                .user(user)
                .accessToken(tokens.accessToken())
                .refreshToken(tokens.refreshToken())
                .serialId(data.id())
                .email(data.email())
                .build();
        return googleCalendarSaver.save(googleCalendar);
    }

    @Transactional
    public void unlink(final Long userId, final Long googleCalenderId){
        User user = userRetriever.findById(userId);
        GoogleCalendar googleCalendar = googleCalendarRetriever.findByIdAndUser(googleCalenderId, user);
        RestClient restClient = RestClient.create();
        restClient.post()
                .uri(GoogleCalendarConstant.GOOGLE_UNLINK_URL + googleCalendar.getAccessToken())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new BusinessException(BusinessErrorCode.GOOGLE_SERVER_ERROR);
                }).onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new BusinessException(BusinessErrorCode.GOOGLE_SERVER_ERROR);
                });
        googleCalendarRemover.remove(googleCalendar);
    }

    @Transactional
    public List<GoogleSchedulesDto> getGoogleCalenders(
            final Long userId,
            final LocalDate startDate,
            final Integer range,
            final CategoriesDto categoriesDto
    ) {
        User user = userRetriever.findById(userId);
        List<GoogleCalendar> googleCalendars = googleCalendarRetriever.findAllByUser(user);
        List<GoogleSchedulesDto> schedules = new ArrayList<>();
        googleCalendars.forEach(
                googleCalender -> {
                    try {
                        schedules.addAll(getEvents(googleCalender, startDate, range, categoriesDto));
                    } catch (IOException e) {
                        reissue(googleCalender);
                        try {
                            schedules.addAll(getEvents(googleCalender, startDate, range, categoriesDto));
                        } catch (IOException ioException) {
                            log.error("Google Calender Error : {}", ioException.getMessage());
                        }
                    }
                }
        );

        return schedules;
    }

    private static List<GoogleSchedulesDto> getEvents(
            final GoogleCalendar googleCalendar,
            final LocalDate startDate,
            final Integer range,
            final CategoriesDto categoriesDto
    ) throws IOException {
        List<GoogleSchedulesDto> schedules = new ArrayList<>();
        Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setJsonFactory(JSON_FACTORY)
                .setTransport(HTTP_TRANSPORT)
                .build()
                .setAccessToken(googleCalendar.getAccessToken());
        Calendar calender = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(GoogleCalendarConstant.APPLICATION_NAME)
                .build();
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
            List<GoogleScheduleDto> googleScheduleDtoList = new ArrayList<>();
            for (Event event : events.getItems()) {
                LocalDateTime start = getLocalDateTime(event.getStart());
                LocalDateTime end = getLocalDateTime(event.getEnd());
                GoogleScheduleDto googleScheduleDto = GoogleScheduleDto.builder()
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
        RestClient restClient = RestClient.create();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(GoogleCalendarConstant.CLIENT_ID, CLIENT_ID);
        body.add(GoogleCalendarConstant.CLIENT_SECRET, CLIENT_SECRET);
        body.add(GoogleCalendarConstant.REFRESH_TOKEN, googleCalendar.getRefreshToken());
        body.add(GoogleCalendarConstant.GRANT_TYPE, GoogleCalendarConstant.REFRESH_TOKEN);
        GoogleTokenDto tokens = restClient.post()
                .uri(GoogleCalendarConstant.GOOGLE_TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new BusinessException(BusinessErrorCode.GOOGLE_SERVER_ERROR);
                }).onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new BusinessException(BusinessErrorCode.GOOGLE_SERVER_ERROR);
                }).body(GoogleTokenDto.class);
        assert tokens != null;
        googleCalendarUpdater.updateTokens(googleCalendar, tokens.accessToken());
    }

    private static LocalDateTime getLocalDateTime(final EventDateTime event) {
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