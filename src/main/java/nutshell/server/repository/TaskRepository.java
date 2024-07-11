package nutshell.server.repository;

import nutshell.server.domain.Task;
import nutshell.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByUserAndId(User user, Long id);

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
}
