package nutshell.server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nutshell.server.exception.code.InternalServerErrorCode;

@Getter
@RequiredArgsConstructor
public class DiscordException extends RuntimeException{
    private final InternalServerErrorCode internalServerErrorCode;
}
