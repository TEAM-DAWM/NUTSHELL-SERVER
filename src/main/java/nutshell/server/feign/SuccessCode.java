package nutshell.server.feign;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode {
    POST_LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공했습니다."),
    POST_REISSUE_SUCCESS(HttpStatus.OK, "액세스 토큰 재발급 완료."),
    POST_LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공하였습니다.")
    ;

    public final HttpStatus httpStatus;
    public final String message;
}
