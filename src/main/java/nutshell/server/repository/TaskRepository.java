package nutshell.server.repository;

import nutshell.server.domain.Task;
import nutshell.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByUserAndId(User user, Long id);
}
