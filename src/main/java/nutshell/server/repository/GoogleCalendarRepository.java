package nutshell.server.repository;

import nutshell.server.domain.GoogleCalendar;
import nutshell.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoogleCalendarRepository extends JpaRepository<GoogleCalendar, Long> {
    Optional<GoogleCalendar> findByIdAndUser(final Long id, final User user);
    List<GoogleCalendar> findAllByUser(final User user);
}
