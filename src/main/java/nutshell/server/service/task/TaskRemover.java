package nutshell.server.service.task;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.repository.TaskRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskRemover {

    public final TaskRepository taskRepository;

    public void deleteTask(Task task){
        taskRepository.delete(task);
    }

}
