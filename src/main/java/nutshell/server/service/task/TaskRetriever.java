package nutshell.server.service.task;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.domain.User;
import nutshell.server.dto.type.Status;
import nutshell.server.exception.NotFoundException;
import nutshell.server.exception.code.NotFoundErrorCode;
import nutshell.server.repository.TaskRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskRetriever {
    public final TaskRepository taskRepository;

    public Task findTaskByTaskId(final Long taskId){
        return taskRepository.findById(taskId).orElseThrow(
                () -> new NotFoundException(NotFoundErrorCode.NOT_FOUND_TASK)
        );
    }

    public List<Task> findAllByUserAndAssignedDateIsNullOrderByCreatedAtDesc(final User user){
        return taskRepository.findAllByUserAndAssignedDateIsNullOrderByCreatedAtDesc(user);
    }

    public List<Task> findAllByUserAndAssignedDateIsNullOrderByCreatedAtAsc(final User user){
        return taskRepository.findAllByUserAndAssignedDateIsNullOrderByCreatedAtAsc(user);
    }

    public List<Task> findAllByUserAndAssignedDateIsNullOrderByTimeDiffAsc(final User user){
        return taskRepository.findAllByUserAndAssignedDateIsNullOrderByTimeDiffAsc(user.getId());
    }

    public List<Task> findAllByUserAndAssignedDateIsNullOrderByTimeDiffDesc(final User user){
        return taskRepository.findAllByUserAndAssignedDateIsNullOrderByTimeDiffDesc(user.getId());
    }

    public List<Task> findAllByUserAndStatusOrderByUpdatedStatusAtDescAssignedDateDesc(final User user, final Status status, final LocalDate targetDate){
        LocalDate tomorrow = targetDate.plusDays(1);
        return taskRepository.findAllByUserAndStatusOrderByUpdatedStatusAtDescAssignedDateDesc(user.getId(), status.toString(), targetDate, tomorrow);
    }
}
