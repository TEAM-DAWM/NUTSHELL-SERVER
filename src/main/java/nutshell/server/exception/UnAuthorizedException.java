package nutshell.server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nutshell.server.exception.code.UnAuthorizedErrorCode;

@Getter
@RequiredArgsConstructor
public class UnAuthorizedException extends RuntimeException {
    private final UnAuthorizedErrorCode errorCode;
}