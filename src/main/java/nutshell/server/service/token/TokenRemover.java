package nutshell.server.service.token;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Token;
import nutshell.server.repository.TokenRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenRemover {
    private final TokenRepository tokenRepository;
    public void deleteToken(final Token token) {tokenRepository.delete(token);}
}
