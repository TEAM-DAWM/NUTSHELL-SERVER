package nutshell.server.repository;

import nutshell.server.domain.Task;
import nutshell.server.domain.User;
import nutshell.server.dto.type.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserAndAssignedDateIsNullOrderByCreatedAtDesc(final User user);
    List<Task> findAllByUserAndAssignedDateIsNullOrderByCreatedAtAsc(final User user);
    List<Task> findAllByStatusAndAssignedDateLessThan(Status status, LocalDate assignedDate);
    Optional<Task> findByUserAndId(final User user, final Long id);

    @Query(
            value = "select * from task t where t.user_id = :userId " +
                    "AND t.assigned_date is null " +
                    "order by abs(extract(epoch from now() - t.dead_line)) asc nulls last"
            , nativeQuery = true
    )
    List<Task> findAllByUserAndAssignedDateIsNullOrderByTimeDiffAsc(final Long userId);

    @Query(
            value = "select * from task t where t.user_id = :userId " +
                    "AND t.assigned_date is null " +
                    "order by abs(extract(epoch from now() - t.dead_line)) desc nulls last"
            , nativeQuery = true
    )
    List<Task> findAllByUserAndAssignedDateIsNullOrderByTimeDiffDesc(final Long userId);

    @Query(
            value = "SELECT * FROM task t WHERE t.user_id = :userId " +
                    "AND t.status = :status " +
                    "AND t.assigned_date IS NOT NULL AND " +
                    "((t.completion_date IS NULL AND t.assigned_date <= :targetDate) " +
                    "OR " +
                    "(t.completion_date IS NOT NULL AND t.completion_date >= :targetDate AND t.completion_date < :tomorrow) " +
                    ") " +
                    "ORDER BY t.updated_status_at DESC NULLS LAST, t.assigned_date DESC"
            , nativeQuery = true
    )
    List<Task> findAllByUserAndStatusOrderByUpdatedStatusAtDescAssignedDateDesc(final Long userId, final String status, final LocalDate targetDate,final LocalDate tomorrow);
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
