package nutshell.server.service.task;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.dto.task.TaskAssignedDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TaskUpdater {

    public void updateAssignedTask(
            final Task task,
            final LocalDate targetDate
    ) {
        task.updateAssignedDate(targetDate);
    }
}
