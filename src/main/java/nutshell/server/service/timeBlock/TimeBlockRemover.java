package nutshell.server.service.timeBlock;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.TimeBlock;
import nutshell.server.repository.TimeBlockRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TimeBlockRemover {
    private final TimeBlockRepository timeBlockRepository;

    @Transactional
    public void remove(final TimeBlock timeBlock) {
        timeBlockRepository.delete(timeBlock);
    }
}
