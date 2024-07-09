package nutshell.server.service.googleCalendar;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.GoogleCalendar;
import nutshell.server.repository.GoogleCalendarRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleCalendarSaver {
    private final GoogleCalendarRepository googleCalendarRepository;
    public GoogleCalendar save(final GoogleCalendar googleCalendar) {
        return googleCalendarRepository.save(googleCalendar);
    }
}
