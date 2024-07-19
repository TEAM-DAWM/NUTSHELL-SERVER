package nutshell.server.service.googleSchedule;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.GoogleSchedule;
import nutshell.server.repository.GoogleScheduleRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleScheduleSaver {
    private final GoogleScheduleRepository googleScheduleRepository;

    public void save(final GoogleSchedule googleSchedule) {
        googleScheduleRepository.save(googleSchedule);
    }
}
