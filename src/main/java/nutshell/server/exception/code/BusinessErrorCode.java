package nutshell.server.exception.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BusinessErrorCode implements DefaultErrorCode{
    DONE_CONFLICT(HttpStatus.OK,"conflict","이미 완료된 작업입니다"),
    IN_PROGRESS_CONFLICT(HttpStatus.OK,"conflict","target date에만 진행 가능합니다"),
    MULTI_CONFLICT(HttpStatus.OK,"conflict","중복된 작업이 있습니다"),
    DAY_CONFLICT(HttpStatus.OK, "conflict", "오늘 할당된 작업이 이미 있습니다."),
    TODO_CONFLICT(HttpStatus.OK,"conflict","오늘 이후에만 진행 가능합니다"),
    GOOGLE_SERVER_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "external", "구글 서버 오류"),
    DATE_CONFLICT(HttpStatus.OK, "conflict", "시작시간이 종료시간보다 늦습니다"),
    DENY_DAY(HttpStatus.OK, "conflict", "해당 날짜에는 작업을 할당할 수 없습니다"),
    BUSINESS_TODAY(HttpStatus.OK, "conflict", "오늘 이전 날짜로는 할당할 수 없습니다"),
    BUSINESS_DUP_DAY(HttpStatus.OK, "conflict", "이미 할당된 날짜입니다"),
    ;
    @JsonIgnore
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
