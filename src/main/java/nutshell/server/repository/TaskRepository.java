package nutshell.server.repository;

import nutshell.server.domain.Task;
import nutshell.server.dto.type.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByStatusAndAssignedDateLessThan(Status status, LocalDate assignedDate);

}
