package nutshell.server.service.task;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.domain.User;
import nutshell.server.exception.NotFoundException;
import nutshell.server.exception.code.NotFoundErrorCode;
import nutshell.server.repository.TaskRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

    public Task findById(final Long id){
        return taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException(NotFoundErrorCode.NOT_FOUND_TASK)
        );
    }
    public List<Task> findAllByUserAndTimeBlocks(User user, LocalDateTime startTime, LocalDateTime endTime) {
        return taskRepository.findAllByUserAndTimeBlocks(user, startTime, endTime);
    }
    public List<Task> findAllByUserAndAssignedDateOrderByCreatedAtDesc(final User user, final LocalDate assignedDate){
        return taskRepository.findAllByUserAndAssignedDateOrderByCreatedAtDesc(user.getId(), assignedDate);
    }

    public List<Task> findAllByUserAndAssignedDateOrderByCreatedAtAsc(final User user, final LocalDate assignedDate){
        return taskRepository.findAllByUserAndAssignedDateOrderByCreatedAtAsc(user.getId(), assignedDate);
    }
    public List<Task> findAllByUserAndAssignedDateOrderByTimeDiffAsc(final User user, final LocalDate assignedDate){
        return taskRepository.findAllByUserAndAssignedDateOrderByTimeDiffAsc(user.getId(), assignedDate);
    }

    public List<Task> findAllByUserAndAssignedDateOrderByTimeDiffDesc(final User user, final LocalDate assignedDate){
        return taskRepository.findAllByUserAndAssignedDateOrderByTimeDiffDesc(user.getId(), assignedDate);
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

    public List<Task> findAllByCustomOrderAndAssignedDateIsNotNull(final Long userId, final LocalDate targetDate, final List<Long> taskList){
        Long[] taskListArray = taskList.toArray(new Long[0]);
        return taskRepository.findAllByCustomOrderAndAssignedDateIsNotNull(userId, targetDate, taskListArray);
    }

    public List<Task> findAllByCustomOrderAndAssignedDateIsNull(final Long userId, final List<Long> taskList){
        Long[] taskListArray = taskList.toArray(new Long[0]);
        return taskRepository.findAllByCustomOrderAndAssignedDateIsNull(userId, taskListArray);
    }
}
