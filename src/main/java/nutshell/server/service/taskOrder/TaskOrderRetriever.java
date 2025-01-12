package nutshell.server.service.taskOrder;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.TaskOrder;
import nutshell.server.repository.TaskOrderRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TaskOrderRetriever {
    private final TaskOrderRepository taskOrderRepository;
    public TaskOrder findById(Long userId, Boolean type, LocalDate targetDate){
        String id = userId + "-" + type;
        if (targetDate != null) {
            id += "-" + targetDate;
        }
        return taskOrderRepository.findById(id).orElse(null);
    }
}
