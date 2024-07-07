package nutshell.server.service.task;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.repository.TaskRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskSaver {
    private final TaskRepository taskRepository;

    public void save(Task task) {
        taskRepository.save(task);
    }
}
