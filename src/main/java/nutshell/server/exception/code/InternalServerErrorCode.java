package nutshell.server.exception.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum InternalServerErrorCode implements DefaultErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "error", "서버 내부 오류입니다."),
    ;

    @JsonIgnore
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}