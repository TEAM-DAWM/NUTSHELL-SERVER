package nutshell.server.service.taskStatus;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.TaskStatus;
import nutshell.server.repository.TaskStatusRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskStatusSaver {
    private final TaskStatusRepository taskStatusRepository;

    public TaskStatus save(TaskStatus taskStatus) {
        return taskStatusRepository.save(taskStatus);
    }

    public void saveAll(List<TaskStatus> taskStatuses) {
        taskStatusRepository.saveAll(taskStatuses);
    }
}
