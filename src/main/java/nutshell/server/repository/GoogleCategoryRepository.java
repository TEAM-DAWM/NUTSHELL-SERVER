package nutshell.server.repository;

import nutshell.server.domain.GoogleCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GoogleCategoryRepository extends CrudRepository<GoogleCategory, String> {
    List<GoogleCategory> findAllByGoogleCalendarId(final Long googleCalendarId);
}
