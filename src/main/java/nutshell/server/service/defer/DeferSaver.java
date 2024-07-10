package nutshell.server.service.defer;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Defer;
import nutshell.server.repository.DeferRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeferSaver {
    private final DeferRepository deferRepository;

    public Defer save(final Defer defer){
        return deferRepository.save(defer);
    }
}
