package nutshell.server.service.googleCalendar;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.GoogleCalendar;
import nutshell.server.repository.GoogleCalendarRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleCalendarRemover {
    private final GoogleCalendarRepository googleCalendarRepository;

    public void remove(final GoogleCalendar googleCalendar) {
        googleCalendarRepository.delete(googleCalendar);
    }
}
