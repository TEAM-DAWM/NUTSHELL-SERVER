package nutshell.server.exception;

import nutshell.server.exception.code.NotFoundErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotFoundException extends RuntimeException{
    private final NotFoundErrorCode errorCode;
}
