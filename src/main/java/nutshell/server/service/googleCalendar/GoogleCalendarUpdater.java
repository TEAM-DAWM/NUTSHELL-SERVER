package nutshell.server.service.googleCalendar;

import nutshell.server.domain.GoogleCalendar;
import org.springframework.stereotype.Component;

@Component
public class GoogleCalendarUpdater {
    public void updateTokens(
            final GoogleCalendar googleCalendar,
            final String accessToken
    ) {
        googleCalendar.updateTokens(accessToken);
    }
}
