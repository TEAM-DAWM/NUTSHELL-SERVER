package nutshell.server.repository;

import nutshell.server.domain.Task;
import nutshell.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    List<Task> findAllByUserAndAssignedDateIsNullOrderByCreatedAtDesc(final User user);

    List<Task> findAllByUserAndAssignedDateIsNullOrderByCreatedAtAsc(final User user);

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
            value = "select * from task t where t.user_id = :userId " +
                    "AND t.assigned_date is null " +
                    "AND t.dead_line <= now() + interval '2 days' " +
                    "AND t.dead_line >= now() " +
                    "order by t.dead_line asc nulls last"
            ,nativeQuery = true
    )
    List<Task> findAllUpcomingTasksByUserWitAssignedStatus(final Long userId);

    @Query(
            value = "select * from task t where t.user_id = :userId " +
                    "AND t.status = 'DEFERRED' AND t.assigned_date is null " +
                    "order by t.dead_line nulls last"
            ,nativeQuery = true
    )
    List<Task> findAllDeferredTasksByUserWithStatus(final Long userId);

}
