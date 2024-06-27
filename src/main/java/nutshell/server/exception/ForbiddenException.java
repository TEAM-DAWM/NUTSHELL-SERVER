package nutshell.server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nutshell.server.exception.code.ForbiddenErrorCode;

@Getter
@RequiredArgsConstructor
public class ForbiddenException extends RuntimeException {
    private final ForbiddenErrorCode errorCode;
}
