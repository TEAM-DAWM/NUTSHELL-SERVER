package nutshell.server.service.googleCalender;

import nutshell.server.domain.GoogleCalender;
import org.springframework.stereotype.Component;

@Component
public class GoogleCalenderUpdater {
    public void updateTokens(
            final GoogleCalender googleCalender,
            final String accessToken
    ) {
        googleCalender.updateTokens(accessToken);
    }
}
