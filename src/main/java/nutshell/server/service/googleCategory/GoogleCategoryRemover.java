package nutshell.server.service.googleCategory;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.GoogleCategory;
import nutshell.server.repository.GoogleCategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleCategoryRemover {
    private final GoogleCategoryRepository googleCategoryRepository;

    public void removeAll(List<GoogleCategory> googleCategories) {
        googleCategoryRepository.deleteAll(googleCategories);
    }
}
