package nutshell.server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nutshell.server.exception.code.IllegalArgumentErrorCode;

@Getter
@RequiredArgsConstructor
public class IllegalArgumentException extends RuntimeException {
    private final IllegalArgumentErrorCode errorCode;
}