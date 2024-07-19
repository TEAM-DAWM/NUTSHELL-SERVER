package nutshell.server.service.googleSchedule;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.GoogleSchedule;
import nutshell.server.repository.GoogleScheduleRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleScheduleRemover {
    private final GoogleScheduleRepository googleScheduleRepository;

    public void removeAll(final List<GoogleSchedule> googleSchedules) {
        googleScheduleRepository.deleteAll(googleSchedules);
    }
}
