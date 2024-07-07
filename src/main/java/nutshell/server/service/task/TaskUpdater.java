package nutshell.server.service.task;

import nutshell.server.domain.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TaskUpdater {

    public void updateAssignedDate(
            final Task task,
            final LocalDate assignedDate
    ){
        task.updateAssignedDate(assignedDate);
    }
}
