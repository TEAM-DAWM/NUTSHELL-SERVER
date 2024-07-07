package nutshell.server.service.token;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Token;
import nutshell.server.repository.TokenRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenSaver {
    private final TokenRepository tokenRepository;
    public void save(final Token token) {tokenRepository.save(token);}
}
