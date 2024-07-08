package nutshell.server.service.task;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.dto.type.Status;
import nutshell.server.repository.TaskRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TaskUpdater {
    private final TaskRepository taskRepository;

    public void updateAssignedTask(
            final Task task,
            final LocalDate targetDate
    ) {
        task.updateAssignedDate(targetDate);
    }

    public void updateStatus(
            final Task task,
            final Status status
    ){
        task.updateStatus(status);

    }
}
