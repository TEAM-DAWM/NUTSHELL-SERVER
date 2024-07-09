package nutshell.server.service.timeBlock;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.domain.TimeBlock;
import nutshell.server.domain.User;
import nutshell.server.dto.timeBlock.response.TimeBlockDto;
import nutshell.server.exception.NotFoundException;
import nutshell.server.exception.code.NotFoundErrorCode;
import nutshell.server.repository.TimeBlockRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TimeBlockRetriever {
    private final TimeBlockRepository timeBlockRepository;

    public TimeBlock findByTaskAndId(
            final Task task,
            final Long id
    ) {
        return timeBlockRepository.findByTaskAndId(task, id).orElseThrow(
                () -> new NotFoundException(NotFoundErrorCode.NOT_FOUND_TIME_BLOCK)
        );
    }
    public Boolean existsByTaskUserAndStartTimeBetweenAndEndTimeBetween(
            final User user,
            final LocalDateTime startTime,
            final LocalDateTime endTime
    ) {
        return timeBlockRepository.existsByTaskUserAndStartTimeBetweenAndEndTimeBetween(user, startTime, endTime);
    }

    public Boolean existsByTaskUserAndStartTimeBetweenAndEndTimeBetweenAndIdNot(
            final User user,
            final Long id,
            final LocalDateTime startTime,
            final LocalDateTime endTime
    ) {
        return timeBlockRepository.existsByTaskUserAndStartTimeBetweenAndEndTimeBetweenAndIdNot(user, id, startTime, endTime);
    }

    public Boolean existsByTaskAndStartTimeBetweenAndEndTimeBetween(
            final Task task,
            final LocalDateTime startTime,
            final LocalDateTime endTime
    ) {
        return timeBlockRepository.existsByTaskAndStartTimeBetweenAndEndTimeBetween(task, startTime, endTime);
    }

    public List<TimeBlockDto> findAllByTaskIdAndTimeRange(
            final Task task,
            final LocalDateTime startTime,
            final LocalDateTime endTime
    ) {
        return timeBlockRepository.findAllByTaskIdAndTimeRange(task, startTime, endTime);
    }
}
