package nutshell.server.service.timeblock;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.domain.TimeBlock;
import nutshell.server.repository.TimeBlockRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TimeBlockRetriever {

    private final TimeBlockRepository timeBlockRepository;

    public TimeBlock findByTaskIdAndTargetDate(final Task task, final LocalDate targetDate){
        LocalDateTime td = targetDate.atStartOfDay();
        LocalDateTime tomorrow = targetDate.atTime(23, 59, 59);
        return timeBlockRepository.findByTaskIdAndTargetDate(task, td, tomorrow).orElse(null);
    }
}