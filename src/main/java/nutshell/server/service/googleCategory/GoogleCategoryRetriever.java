package nutshell.server.service.googleCategory;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.GoogleCategory;
import nutshell.server.repository.GoogleCategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleCategoryRetriever {
    private final GoogleCategoryRepository googleCategoryRepository;

    public List<GoogleCategory> findAllByGoogleCalendarId(final Long googleCalendarId) {
        return googleCategoryRepository.findAllByGoogleCalendarId(googleCalendarId);
    }
}
