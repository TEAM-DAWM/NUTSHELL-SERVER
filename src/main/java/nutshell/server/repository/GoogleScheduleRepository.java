package nutshell.server.repository;

import nutshell.server.domain.GoogleSchedule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GoogleScheduleRepository extends CrudRepository<GoogleSchedule, String> {
    List<GoogleSchedule> findAllByGoogleCalendarId(Long googleCalendarId);
}
