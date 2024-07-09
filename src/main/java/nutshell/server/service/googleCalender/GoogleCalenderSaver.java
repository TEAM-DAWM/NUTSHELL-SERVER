package nutshell.server.service.googleCalender;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.GoogleCalender;
import nutshell.server.repository.GoogleCalenderRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleCalenderSaver {
    private final GoogleCalenderRepository googleCalenderRepository;
    public GoogleCalender save(final GoogleCalender googleCalender) {
        return googleCalenderRepository.save(googleCalender);
    }
}
