package nutshell.server.service.taskStatus;

import nutshell.server.domain.TaskStatus;
import nutshell.server.dto.type.Status;
import org.springframework.stereotype.Component;

@Component
public class TaskStatusUpdater {
    public void updateStatus(final TaskStatus taskStatus, final Status status){
        taskStatus.updateStatus(status);
    }
}
