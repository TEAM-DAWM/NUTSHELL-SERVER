package nutshell.server.service.task;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.domain.User;
import nutshell.server.exception.NotFoundException;
import nutshell.server.exception.code.NotFoundErrorCode;
import nutshell.server.repository.TaskRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskRetriever {
    public final TaskRepository taskRepository;

    public Task findById(final Long id){
        return taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException(NotFoundErrorCode.NOT_FOUND_TASK)
        );
    }
    public Task findByUserAndId(final User user, final Long id){
        return taskRepository.findByUserAndId(user, id).orElseThrow(
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
}
