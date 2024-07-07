package nutshell.server.exception.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotFoundErrorCode implements DefaultErrorCode{
    NOT_FOUND_END_POINT(HttpStatus.NOT_FOUND, "error", "존재하지 않는 API입니다."),
    NOT_FOUND_GOOGLE_CALENDER(HttpStatus.NOT_FOUND, "error", "존재하지 않는 구글 연동 입니다."),
    NOT_FOUND_TIME_BLOCK(HttpStatus.NOT_FOUND, "error", "존재하지 않는 TimeBlock 입니다."),
    NOT_FOUND_TASK(HttpStatus.NOT_FOUND, "error", "존재하지 않는 Task 입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "error", "존재하지 않는 User 입니다."),
    ;

    @JsonIgnore
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}