package nutshell.server.repository;

import nutshell.server.domain.GoogleCalender;
import nutshell.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoogleCalenderRepository extends JpaRepository<GoogleCalender, Long> {
    Optional<GoogleCalender> findByIdAndUser(final Long id, final User user);
    List<GoogleCalender> findAllByUser(final User user);
}
