package nutshell.server.repository;

import nutshell.server.domain.Task;
import nutshell.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import nutshell.server.dto.type.Status;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByStatusAndAssignedDateLessThan(Status status, LocalDate assignedDate);
    Optional<Task> findByUserAndId(final User user, final Long id);

    @Query(
            value = "select * from task t where t.user_id = :userId " +
                    "AND t.assigned_date is null " +
                    "AND t.dead_line <= now() + interval '2 days' " +
                    "order by t.dead_line asc nulls last"
            ,nativeQuery = true
    )
    List<Task> findAllUpcomingTasksByUserWitAssignedStatus(final Long userId);

    @Query(
            value = "select * from task t where t.user_id = :userId " +
                    "AND t.status = 'IN_PROGRESS' " +
                    "order by t.dead_line nulls last"
            ,nativeQuery = true
    )
    List<Task> findAllInprogressTasksByUserWithStatus(final Long userId);

    @Query(
            value = "select * from task t where t.user_id = :userId " +
                    "AND t.status = 'DEFERRED' " +
                    "order by t.dead_line nulls last"
            ,nativeQuery = true
    )
    List<Task> findAllDeferredTasksByUserWithStatus(final Long userId);
}
