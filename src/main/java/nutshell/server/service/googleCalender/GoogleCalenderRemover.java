package nutshell.server.service.googleCalender;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.GoogleCalender;
import nutshell.server.repository.GoogleCalenderRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleCalenderRemover {
    private final GoogleCalenderRepository googleCalenderRepository;

    public void remove(final GoogleCalender googleCalender) {
        googleCalenderRepository.delete(googleCalender);
    }
}
