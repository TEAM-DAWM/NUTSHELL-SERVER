package nutshell.server.exception.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ForbiddenErrorCode implements DefaultErrorCode{
    //인증된 클라이언트가 권한이 없는 자원에 접근할 때 응답하는 상태 코드
    // 근데 우리 프로젝트에는 이게 필요한가??
    FORBIDDEN(HttpStatus.FORBIDDEN, "error", "접근 권한이 없습니다."),
    ;

    @JsonIgnore
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}