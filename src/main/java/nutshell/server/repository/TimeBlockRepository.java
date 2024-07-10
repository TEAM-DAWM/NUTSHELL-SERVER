package nutshell.server.repository;

import nutshell.server.domain.Task;
import nutshell.server.domain.TimeBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TimeBlockRepository extends JpaRepository<TimeBlock, Long> {

    @Query(value = "SELECT t FROM TimeBlock t WHERE t.task = :task " +
            "and t.startTime >= :targetDate and t.startTime <= :tomorrow " +
            "and t.endTime >= :targetDate and t.endTime <= :tomorrow")
    Optional<TimeBlock> findByTaskIdAndTargetDate(
            final Task task,
            final LocalDateTime targetDate,
            final LocalDateTime tomorrow
    );
}
