package nutshell.server.service.taskStatus;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.TaskStatus;
import nutshell.server.repository.TaskStatusRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskStatusSaver {
    private final TaskStatusRepository taskStatusRepository;

    public TaskStatus save(TaskStatus taskStatus) {
        return taskStatusRepository.save(taskStatus);
    }
}
