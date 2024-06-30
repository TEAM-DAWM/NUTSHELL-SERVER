package nutshell.server.advice;


import lombok.extern.slf4j.Slf4j;
import nutshell.server.exception.BusinessException;
import nutshell.server.exception.ForbiddenException;
import nutshell.server.exception.NotFoundException;
import nutshell.server.exception.UnAuthorizedException;
import nutshell.server.exception.code.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 요청은 정상이나 비즈니스 중 실패가 있는 경우
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BusinessErrorCode> handleBusinessException(BusinessException e) {
        log.error("GlobalExceptionHandler catch BusinessException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(e.getErrorCode());
    }

    // DB에서 데이터를 찾지 못한 경우
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<NotFoundErrorCode> handleNotFoundException(NotFoundException e){
        log.error("GlobalExceptionHandler catch NotFoundException : {}", e.getErrorCode().getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(e.getErrorCode());
    }

    // 존재하지 않는 요청에 대한 예외
    @ExceptionHandler(value={NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<NotFoundErrorCode> handleNoPageFoundException(Exception e) {
        log.error("GlobalExceptionHandler catch NoHandlerFoundException : {}", e.getMessage());
        return ResponseEntity
                .status(NotFoundErrorCode.NOT_FOUND_END_POINT.getHttpStatus())
                .body(NotFoundErrorCode.NOT_FOUND_END_POINT);
    }

    // 기본 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<InternalServerErrorCode> handleException(Exception e) {
        log.error("handleException() in GlobalExceptionHandler throw Exception : {}", e.getMessage());
        return ResponseEntity
                .status(InternalServerErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(InternalServerErrorCode.INTERNAL_SERVER_ERROR);
    }

    // 유효하지 않은 인자가 들어온 경우 (@Valid 사용 시 수행)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<IllegalArgumentErrorCode> handleException(MethodArgumentNotValidException e) {
        log.error("handleException() in GlobalExceptionHandler throw MethodArgumentNotValidException : {}", e.getMessage());
        return ResponseEntity
                .status(IllegalArgumentErrorCode.INVALID_ARGUMENTS.getHttpStatus())
                .body(IllegalArgumentErrorCode.INVALID_ARGUMENTS);
    }

    //권한이 없는 경우
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ForbiddenErrorCode> handleException(ForbiddenException e) {
        log.error("handleException() in GlobalExceptionHandler throw ForbiddenException : {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(e.getErrorCode());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<UnAuthorizedErrorCode> handleException(UnAuthorizedException e) {
        log.error("handleException() in GlobalExceptionHandler throw UnAuthorizedException : {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(e.getErrorCode());
    }

}