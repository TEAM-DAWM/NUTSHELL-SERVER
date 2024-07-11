package nutshell.server.exception.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BusinessErrorCode implements DefaultErrorCode{
    BUSINESS_TEST(HttpStatus.OK,"conflict","선착순 마감됐어요"),
    BUSINESS_TODAY(HttpStatus.OK, "conflict", "오늘 이후로 선택할 수 있습니다."),
    ;
    @JsonIgnore
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
