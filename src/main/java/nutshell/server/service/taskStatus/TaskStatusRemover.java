package nutshell.server.service.taskStatus;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.TaskStatus;
import nutshell.server.repository.TaskStatusRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskStatusRemover {
    private final TaskStatusRepository taskStatusRepository;

    public void remove(final TaskStatus taskStatus){
        taskStatusRepository.delete(taskStatus);
    }

    public void removeAll(final List<TaskStatus> taskStatuses){
        taskStatusRepository.deleteAll(taskStatuses);
    }
}
