package nutshell.server.service.googleCategory;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.GoogleCategory;
import nutshell.server.repository.GoogleCategoryRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleCategorySaver {
    private final GoogleCategoryRepository googleCategoryRepository;

    public void save(final GoogleCategory googleCategory) {
        googleCategoryRepository.save(googleCategory);
    }
}
