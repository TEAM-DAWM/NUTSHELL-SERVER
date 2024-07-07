package nutshell.server.exception.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotFoundErrorCode implements DefaultErrorCode{
    NOT_FOUND_END_POINT(HttpStatus.NOT_FOUND, "error", "존재하지 않는 API입니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "error","리프레시 토큰을 찾을 수 없습니다."),
    ;

    @JsonIgnore
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}