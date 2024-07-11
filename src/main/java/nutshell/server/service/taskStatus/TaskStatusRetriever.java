package nutshell.server.service.taskStatus;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.domain.TaskStatus;
import nutshell.server.domain.User;
import nutshell.server.dto.type.Status;
import nutshell.server.exception.NotFoundException;
import nutshell.server.exception.code.NotFoundErrorCode;
import nutshell.server.repository.TaskStatusRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskStatusRetriever {
    private final TaskStatusRepository taskStatusRepository;

    public TaskStatus findByTaskAndTargetDate(
            final Task task,
            final LocalDate targetDate
    ) {
        return taskStatusRepository.findByTaskAndTargetDateAndStatusNot(task, targetDate, Status.DEFERRED)
                .orElseThrow(
                        () -> new NotFoundException(NotFoundErrorCode.NOT_FOUND_TASK_DAY)
                );
    }

    public List<TaskStatus> findAllByTaskAndTargetDateGreaterThan(
            final Task task,
            final LocalDate targetDate
    ) {
        return taskStatusRepository.findAllByTaskAndTargetDateGreaterThanAndStatusNot(task, targetDate, Status.DEFERRED);
    }

    public List<TaskStatus> findAllByTask(
            final Task task,
            final LocalDate targetDate
    ) {
        return taskStatusRepository.findAllByTaskAndStatusNotAndTargetDateNot(task, Status.DEFERRED, targetDate);
    }

    public Boolean existsByTaskAndTargetDate(
            final Task task,
            final LocalDate targetDate
    ) {
        return taskStatusRepository.existsByTaskAndTargetDateAndStatusNot(task, targetDate, Status.DEFERRED);
    }

    public List<TaskStatus> findAllByTargetDate(
            final LocalDate targetDate
    ) {
        return taskStatusRepository.findAllByTargetDate(targetDate);
    }

    public List<TaskStatus> findAllByTargetDateAndStatusDesc(
            final User user,
            final LocalDate targetDate,
            final Status status
    ) {
        return taskStatusRepository.findAllByTargetDateAndStatusDesc(user, targetDate, status);
    }
}
