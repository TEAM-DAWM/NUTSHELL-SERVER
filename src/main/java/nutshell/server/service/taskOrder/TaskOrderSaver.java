package nutshell.server.service.taskOrder;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.TaskOrder;
import nutshell.server.repository.TaskOrderRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskOrderSaver {
    private final TaskOrderRepository taskOrderRepository;
    public TaskOrder save(final TaskOrder taskOrder){
        return taskOrderRepository.save(taskOrder);
    }
}
