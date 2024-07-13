package nutshell.server.service.token;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Token;
import nutshell.server.exception.NotFoundException;
import nutshell.server.exception.code.NotFoundErrorCode;
import nutshell.server.repository.TokenRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenRetriever {
    private final TokenRepository tokenRepository;

    public Token findById(final Long userId){
        return tokenRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(NotFoundErrorCode.NOT_FOUND_REFRESH_TOKEN)
        );
    }
}
