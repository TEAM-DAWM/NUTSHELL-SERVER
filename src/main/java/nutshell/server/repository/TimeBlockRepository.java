package nutshell.server.repository;

import nutshell.server.domain.Task;
import nutshell.server.domain.TimeBlock;
import nutshell.server.dto.timeBlock.response.TimeBlockDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeBlockRepository extends JpaRepository<TimeBlock, Long> {
    @Query(value="select count(*) > 0 from time_block t " +
            "where t.task_id = :taskId and " +
            "t.start_time between :startTime and :endTime or " +
            "t.end_time between :startTime and :endTime limit 1",
            nativeQuery=true)
    Boolean existsByTaskAndStartTimeBetweenAndEndTimeBetween(
            final Long taskId,
            final LocalDateTime startTime,
            final LocalDateTime endTime
    );

    @Query("SELECT new nutshell.server.dto.timeBlock.response.TimeBlockDto(t.id, t.startTime, t.endTime) " +
            "FROM TimeBlock t " +
            "WHERE t.task = :task " +
            "AND t.startTime between :startTime and :endTime " +
            "AND t.endTime between :startTime and :endTime")
    List<TimeBlockDto> findAllByTaskIdAndTimeRange(
            final Task task,
            final LocalDateTime startTime,
            final LocalDateTime endTime
    );

    Optional<TimeBlock> findByTaskAndId(final Task task, final Long id);
}
