package nutshell.server.repository;

import nutshell.server.domain.Task;
import nutshell.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByUserAndId(final User user, final Long aLong);

    @Query(value="select t from Task t where t.user = :user " +
            "and exists (select tb from TimeBlock tb " +
            "where tb.task = t and tb.startTime between :startTime and :endTime " +
            "and tb.endTime between :startTime and :endTime)")
    List<Task> findAllByUserAndTimeBlocks(final User user, LocalDateTime startTime, LocalDateTime endTime);
    @Query(
            value = "select * from task t where t.user_id = :userId " +
                    "AND t.assigned_date is not null " +
                    "AND ((t.end_date is null and :assignedDate >= t.assigned_date) or :assignedDate between t.assigned_date and t.end_date) " +
                    "order by t.created_at desc"
            , nativeQuery = true
    )
    List<Task> findAllByUserAndAssignedDateOrderByCreatedAtDesc(final Long userId, final LocalDate assignedDate);
    @Query(
            value = "select * from task t where t.user_id = :userId " +
                    "AND t.assigned_date is not null " +
                    "AND ((t.end_date is null and :assignedDate >= t.assigned_date) or :assignedDate between t.assigned_date and t.end_date) " +
                    "order by t.created_at asc"
            , nativeQuery = true
    )
    List<Task> findAllByUserAndAssignedDateOrderByCreatedAtAsc(final Long userId, final LocalDate assignedDate);

    @Query(
            value = "select * from task t where t.user_id = :userId " +
                    "AND t.assigned_date is not null " +
                    "AND ((t.end_date is null and :assignedDate >= t.assigned_date) or :assignedDate between t.assigned_date and t.end_date) " +
                    "order by abs(current_date - t.dead_line_date) asc nulls last, " +
                    "abs(extract(epoch from current_time - t.dead_line_time)) asc nulls last"
            , nativeQuery = true
    )
    List<Task> findAllByUserAndAssignedDateOrderByTimeDiffAsc(final Long userId, final LocalDate assignedDate);

    @Query(
            value = "select * from task t where t.user_id = :userId " +
                    "AND t.assigned_date is not null " +
                    "AND ((t.end_date is null and :assignedDate >= t.assigned_date) or :assignedDate between t.assigned_date and t.end_date) " +
                    "order by abs(current_date - t.dead_line_date) desc nulls last, " +
                    "abs(extract(epoch from current_time - t.dead_line_time)) desc nulls last"
            , nativeQuery = true
    )
    List<Task> findAllByUserAndAssignedDateOrderByTimeDiffDesc(final Long userId, final LocalDate assignedDate);
    List<Task> findAllByUserAndAssignedDateIsNullOrderByCreatedAtDesc(final User user);

    List<Task> findAllByUserAndAssignedDateIsNullOrderByCreatedAtAsc(final User user);

    @Query(
            value = "select * from task t where t.user_id = :userId " +
                    "AND t.assigned_date is null " +
                    "order by abs(current_date - t.dead_line_date) asc nulls last, " +
                    "abs(extract(epoch from current_time - t.dead_line_time)) asc nulls last"
            , nativeQuery = true
    )
    List<Task> findAllByUserAndAssignedDateIsNullOrderByTimeDiffAsc(final Long userId);

    @Query(
            value = "select * from task t where t.user_id = :userId " +
                    "AND t.assigned_date is null " +
                    "order by abs(current_date - t.dead_line_date) desc nulls last, " +
                    "abs(extract(epoch from current_time - t.dead_line_time)) desc nulls last"
            , nativeQuery = true
    )
    List<Task> findAllByUserAndAssignedDateIsNullOrderByTimeDiffDesc(final Long userId);

    @Query(
            value = "SELECT * FROM task t " +
            "WHERE t.user_id = :userId " +
            "AND t.assigned_date IS NOT NULL " +
            "AND ((t.end_date IS NULL AND :targetDate >= t.assigned_date) OR :targetDate BETWEEN t.assigned_date AND t.end_date) " +
            "ORDER BY ARRAY_POSITION(CAST(:taskList AS BIGINT[]), t.id)",
    nativeQuery = true
    )
    List<Task> findAllByCustomOrderAndAssignedDateIsNotNull(
            final Long userId,
            final LocalDate targetDate,
            final Long[] taskList
    );
    @Query(
            value = "select * from task t where t.user_id = :userId " +
                    "AND t.assigned_date is null " +
                    "ORDER BY ARRAY_POSITION(CAST(:taskList AS BIGINT[]), t.id)"
            ,nativeQuery = true
    )
    List<Task> findAllByCustomOrderAndAssignedDateIsNull(final Long userId, final Long[] taskList);
}
