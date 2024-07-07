package nutshell.server.service.task;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.domain.User;
import nutshell.server.exception.NotFoundException;
import nutshell.server.exception.code.NotFoundErrorCode;
import nutshell.server.repository.TaskRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskRetriever {
    private final TaskRepository taskRepository;

    public Task findByUserAndId(final User user, final Long taskId) {
        return taskRepository.findByUserAndId(user, taskId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorCode.NOT_FOUND_TASK));
    }
    public List<Task> findAllByUserAndTimeBlocks(User user, LocalDateTime startTime, LocalDateTime endTime) {
        return taskRepository.findAllByUserAndTimeBlocks(user, startTime, endTime);
    }
}
