package nutshell.server.repository;

import nutshell.server.domain.Task;
import nutshell.server.domain.TaskStatus;
import nutshell.server.domain.TimeBlock;
import nutshell.server.domain.User;
import nutshell.server.dto.timeBlock.response.TimeBlockDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeBlockRepository extends JpaRepository<TimeBlock, Long> {
    @Query(value="select count(t) > 0 from TimeBlock t " +
            "where t.taskStatus.task.user = :user and " +
            "t.startTime between :startTime and :endTime or " +
            "t.endTime between :startTime and :endTime"
            )
    Boolean existsByTaskUserAndStartTimeBetweenAndEndTimeBetween(
            final User user,
            final LocalDateTime startTime,
            final LocalDateTime endTime
    );

    @Query(value="select count(t) > 0 from TimeBlock t " +
            "where t.taskStatus.task.user = :user and " +
            "t.id != :id and " +
            "(t.startTime between :startTime and :endTime or " +
            "t.endTime between :startTime and :endTime)"
    )
    Boolean existsByTaskUserAndStartTimeBetweenAndEndTimeBetweenAndIdNot(
            final User user,
            final Long id,
            final LocalDateTime startTime,
            final LocalDateTime endTime
    );

    @Query(value="select exists(select t from TimeBlock t " +
            "where t.taskStatus.task = :task and " +
            "t.startTime between :startTime and :endTime and " +
            "t.endTime between :startTime and :endTime)"
    )
    Boolean existsByTaskAndStartTimeBetweenAndEndTimeBetween(
            final Task task,
            final LocalDateTime startTime,
            final LocalDateTime endTime
    );


    @Query("SELECT new nutshell.server.dto.timeBlock.response.TimeBlockDto(t.id, t.startTime, t.endTime) " +
            "FROM TimeBlock t " +
            "WHERE t.taskStatus.task = :task " +
            "AND t.startTime between :startTime and :endTime " +
            "AND t.endTime between :startTime and :endTime")
    List<TimeBlockDto> findAllByTaskIdAndTimeRange(
            final Task task,
            final LocalDateTime startTime,
            final LocalDateTime endTime
    );

    @Query(value = "SELECT t from TimeBlock t WHERE t.taskStatus.task = :task AND t.id = :id")
    Optional<TimeBlock> findByTaskAndId(final Task task, final Long id);

    @Query( value = "SELECT t from TimeBlock t WHERE t.taskStatus.task = :task " +
            "AND t.startTime >= :startOfDay AND t.startTime <= :endOfDay " +
            "AND t.endTime >= :startOfDay AND t.endTime <= :endOfDay"
    )
    Optional<TimeBlock> findByTaskIdAndTargetDate(final Task task, final LocalDateTime startOfDay, final LocalDateTime endOfDay);

    Optional<TimeBlock> findByTaskStatus(final TaskStatus taskStatus);
}
