package nutshell.server.service.timeBlock;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.TimeBlock;
import nutshell.server.repository.TimeBlockRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimeBlockRemover {
    private final TimeBlockRepository timeBlockRepository;

    public void remove(final TimeBlock timeBlock) {
        timeBlockRepository.delete(timeBlock);
    }
}
