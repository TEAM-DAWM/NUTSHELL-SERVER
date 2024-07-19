package nutshell.server.service.googleSchedule;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import lombok.RequiredArgsConstructor;
import nutshell.server.constant.GoogleConstant;
import nutshell.server.domain.GoogleCalendar;
import nutshell.server.domain.GoogleCategory;
import nutshell.server.domain.GoogleSchedule;
import nutshell.server.dto.googleCalender.response.GoogleSchedulesDto;
import nutshell.server.service.googleCategory.GoogleCategoryRemover;
import nutshell.server.service.googleCategory.GoogleCategoryRetriever;
import nutshell.server.service.googleCategory.GoogleCategorySaver;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleScheduleService {
    private final GoogleCategoryRemover googleCategoryRemover;
    private final GoogleCategorySaver googleCategorySaver;
    private final GoogleCategoryRetriever googleCategoryRetriever;
    private final GoogleScheduleRemover googleScheduleRemover;
    private final GoogleScheduleSaver googleScheduleSaver;
    private final GoogleScheduleRetriever googleScheduleRetriever;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final NetHttpTransport HTTP_TRANSPORT;
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public void syncCalendar(final GoogleCalendar googleCalendar) throws IOException {
        googleScheduleRemover.removeAll(googleScheduleRetriever.findAllByGoogleCalendarId(googleCalendar.getId()));
        googleCategoryRemover.removeAll(googleCategoryRetriever.findAllByGoogleCalendarId(googleCalendar.getId()));
        Calendar calender = getCalendar(googleCalendar);
        CalendarList calendarList = calender.calendarList().list().execute();
        List<CalendarListEntry> items = calendarList.getItems();
        for (CalendarListEntry calendarListEntry : items) {
            String calendarId = calendarListEntry.getId();
            String calendars = calendarListEntry.getSummary();
            String color = calendarListEntry.getBackgroundColor();
            googleCategorySaver.save(
                    GoogleCategory.builder()
                            .id(googleCalendar.getId() + ":" + calendarId)
                            .name(calendars)
                            .color(color)
                            .googleCalendarId(googleCalendar.getId())
                            .build()
            );
            Events events = calender.events().list(calendarId).execute();
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
            googleScheduleSaver.save(
                    GoogleSchedule.builder()
                            .id(googleCalendar.getId() + ":" + calendarId)
                            .name(calendars)
                            .color(color)
                            .googleCalendarId(googleCalendar.getId())
                            .googleCategoryId(calendarId)
                            .schedules(googleScheduleDtoList)
                            .build()
            );
        }
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
