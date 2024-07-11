package nutshell.server.repository;

import nutshell.server.domain.Task;
import nutshell.server.domain.TimeBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeBlockRepository extends JpaRepository<TimeBlock, Long> {

    @Query( value = "SELECT t from TimeBlock t WHERE t.task = :task " +
            "AND t.startTime >= :startOfDay AND t.startTime <= :endOfDay " +
            "AND t.endTime >= :startOfDay AND t.endTime <= :endOfDay"
    )
    Optional<TimeBlock> findByTaskIdAndTargetDate(final Task task, final LocalDateTime startOfDay, final LocalDateTime endOfDay);
}
