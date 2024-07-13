package nutshell.server.exception.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BusinessErrorCode implements DefaultErrorCode{
    TIME_CONFLICT(HttpStatus.OK, "conflict", "시작 시간은 종료 시간 이전이어야 합니다."),
    DUP_TIMEBLOCK_CONFLICT(HttpStatus.OK,"conflict","지정된 시간 범위 내에 이미 TimeBlock이 있습니다."),
    DUP_DAY_TIMEBLOCK_CONFLICT(HttpStatus.OK, "conflict", "지정된 날짜의 작업에 대한 TimeBlock이 이미 있습니다."),
    DENY_DAY_TIMEBLOCK_CONFLICT(HttpStatus.OK, "conflict", "지정된 날짜에 대한 TimeBlock을 생성할 수 없습니다."),
    DUP_DAY_CONFLICT(HttpStatus.OK, "conflict", "지정된 날짜에 이미 Task가 할당되어 있습니다."),
    BUSINESS_TODAY(HttpStatus.OK, "conflict", "오늘 이전 날짜로는 Task를 할당할 수 없습니다."),
    BUSINESS_PERIOD(HttpStatus.OK,"conflict","올바른 기간을 설정해 주세요."),
    GOOGLE_CALENDAR_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "external", "Google 캘린더 이벤트를 검색하지 못했습니다."),
    GOOGLE_SERVER_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "external", "구글 서버 내부 오류입니다."),
    ;
    @JsonIgnore
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
