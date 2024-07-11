package nutshell.server.repository;

import nutshell.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySerialIdAndEmail(final String serialId, final String email);
    Optional<User> findById(final Long userId);
}
