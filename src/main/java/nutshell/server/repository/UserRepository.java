package nutshell.server.repository;

import nutshell.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySerialIdAndEmail(String serialId, String email);

}