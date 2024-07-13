package nutshell.server.exception.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum NotFoundErrorCode implements DefaultErrorCode{
    NOT_FOUND_TASK(HttpStatus.NOT_FOUND, "error", "사용자에게 해당 ID를 가진 Task가 존재하지 않습니다."),
    NOT_FOUND_END_POINT(HttpStatus.NOT_FOUND, "error", "존재하지 않는 API입니다."),
    NOT_FOUND_GOOGLE_CALENDER(HttpStatus.NOT_FOUND, "error", "이 사용자에게 해당 ID를 가진 구글 캘린더가 존재하지 않습니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "error","RefreshToken을 찾을 수 없습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND,"error","해당 ID를 가진 사용자가 존재하지 않습니다."),
    NOT_FOUND_TIME_BLOCK(HttpStatus.NOT_FOUND, "error", "존재하지 않는 TimeBlock입니다."),
    NOT_FOUND_TASK_TYPE(HttpStatus.NOT_FOUND,"error","해당하는 Task의 type을 찾을 수 없습니다."),
    NOT_FOUND_TASK_DAY(HttpStatus.NOT_FOUND,"error","지정한 날짜의 Task를 찾을 수 없습니다."),
    ;

    @JsonIgnore
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}