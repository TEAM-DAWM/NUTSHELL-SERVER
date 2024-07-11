package nutshell.server.repository;

import nutshell.server.domain.Task;
import nutshell.server.domain.TaskStatus;
import nutshell.server.dto.type.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    //Task를 상속 받고 있고 targetDate보다 미래인 것들 중 Deferred 상태가 아닌 것들을 찾아 반환
    List<TaskStatus> findAllByTaskAndTargetDateGreaterThanAndStatusNot(
            final Task task,
            final LocalDate targetDate,
            final Status status
    );
    //Task를 상속 받고 있고 해당 targetDate를 가지고 있으며 Deferred 상태가 아닌 것이 있는지 반환
    Boolean existsByTaskAndTargetDateAndStatusNot(
            final Task task,
            final LocalDate targetDate,
            final Status status
    );
    //Task를 상속 받고 있고 해당 targetDate를 가지고 있으며 Deferred 상태가 아닌 것을 찾아 반환
    Optional<TaskStatus> findByTaskAndTargetDateAndStatusNot(final Task task, final LocalDate targetDate, final Status status);

    //Task를 상속 받고 있고 해당 targetDate를 가지고 있는 것들을 반환
    List<TaskStatus> findAllByTargetDate(final LocalDate TargetDate);

    //Task를 상속 받고 있고 해당 targetDate가 아니며 Deferred 상태가 아닌 것들을 찾아 반환
    List<TaskStatus> findAllByTaskAndStatusNotAndTargetDateNot(final Task task, final Status status, final LocalDate targetDate);


}
