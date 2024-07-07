package nutshell.server.service.timeBlock;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.TimeBlock;
import nutshell.server.repository.TimeBlockRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimeBlockSaver {
    private final TimeBlockRepository timeBlockRepository;

    public TimeBlock save(final TimeBlock timeBlock) {
        return timeBlockRepository.save(timeBlock);
    }
}
