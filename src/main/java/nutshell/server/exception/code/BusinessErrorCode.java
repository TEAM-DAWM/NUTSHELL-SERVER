package nutshell.server.exception.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BusinessErrorCode implements DefaultErrorCode{
    WRONG_ENTRY_POINT(HttpStatus.BAD_REQUEST, "error", "잘못된 요청입니다."),
    TIME_CONFLICT(HttpStatus.OK, "conflict", "시작 시간은 종료 시간 이전이어야 합니다."),
    NOT_SAME_DATE_CONFLICT(HttpStatus.OK, "conflict", "시작 시간과 종료 시간의 날짜가 같아야 합니다."),
    DUP_TIMEBLOCK_CONFLICT(HttpStatus.OK,"conflict","지정된 시간 범위 내에 이미 타임 블록이 있습니다."),
    DUP_DAY_TIMEBLOCK_CONFLICT(HttpStatus.OK, "conflict", "지정된 날짜의 작업에 대한 타임 블록이 이미 있습니다."),
    DENY_DAY_TIMEBLOCK_CONFLICT(HttpStatus.OK, "conflict", "지정된 날짜에 대한 타임 블록을 생성할 수 없습니다."),
    DUP_DAY_CONFLICT(HttpStatus.OK, "conflict", "지정된 날짜에 이미 할 일이 할당되어 있습니다."),
    BUSINESS_TODAY(HttpStatus.OK, "conflict", "오늘 이전 날짜로는 할 일 할당할 수 없습니다."),
    BUSINESS_PERIOD(HttpStatus.OK,"conflict","올바른 기간을 설정해 주세요."),
    GOOGLE_SERVER_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "external", "구글 서버 내부 오류입니다."),
    GOOGLE_SERVER_EXIST(HttpStatus.OK, "conflict", "이미 연동한 구글 계정입니다."),
    ;
    @JsonIgnore
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
