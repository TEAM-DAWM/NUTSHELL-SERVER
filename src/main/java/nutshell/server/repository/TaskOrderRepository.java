package nutshell.server.repository;

import nutshell.server.domain.TaskOrder;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TaskOrderRepository  extends CrudRepository<TaskOrder, String> {
    Optional<TaskOrder> findById(final String id);
}
