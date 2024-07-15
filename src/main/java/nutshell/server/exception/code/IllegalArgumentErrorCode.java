package nutshell.server.exception.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum IllegalArgumentErrorCode implements DefaultErrorCode {
    INVALID_RANGE(HttpStatus.BAD_REQUEST,"validation","range는 0보다 커야 합니다."),
    INVALID_STATUS_ARGUMENTS(HttpStatus.BAD_REQUEST,"error","Staging Area에서의 Task는 완료, 미완료 상태값 가질 수 있습니다."),
    INVALID_ARGUMENTS(HttpStatus.BAD_REQUEST, "error", "인자의 형식이 올바르지 않습니다."),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "error", "날짜 형식이 올바르지 않습니다."),
    ;

    @JsonIgnore
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}