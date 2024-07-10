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

    public Task findByUserAndId(final User user, final Long id){
        return taskRepository.findByUserAndId(user, id).orElseThrow(
                () -> new NotFoundException(NotFoundErrorCode.NOT_FOUND_TASK)
        );
    }

    public List<Task> findAllByStatusAndAssignedDateLessThan(){
        return taskRepository.findAllByStatusAndAssignedDateLessThan(Status.TODO, LocalDate.now());
    }

    public List<Task> findAllUpcomingTasksByUserWitAssignedStatus(final Long userId){
        return taskRepository.findAllUpcomingTasksByUserWitAssignedStatus(userId);
    }

    public List<Task> findAllInprogressTasksByUserWithStatus(final Long userId){
        return taskRepository.findAllInprogressTasksByUserWithStatus(userId);
    }

    public List<Task> findAllDeferredTasksByUserWithStatus(final Long userId){
        return taskRepository.findAllDeferredTasksByUserWithStatus(userId);
    }


}
