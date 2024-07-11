package nutshell.server.repository;

import nutshell.server.domain.Task;
import nutshell.server.domain.TaskStatus;
import nutshell.server.dto.type.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    List<TaskStatus> findAllByTaskAndTargetDateGreaterThanAndStatusNot(
            final Task task,
            final LocalDate targetDate,
            final Status status
    );
    Boolean existsByTaskAndTargetDateAndStatusNot(
            final Task task,
            final LocalDate targetDate,
            final Status status
    );
    Optional<TaskStatus> findByTaskAndTargetDateAndStatusNot(final Task task, final LocalDate targetDate, final Status status);

    List<TaskStatus> findAllByTargetDate(final LocalDate TargetDate);

    List<TaskStatus> findAllByTaskAndStatusNotAndTargetDateNot(final Task task, final Status status, final LocalDate targetDate);


}
