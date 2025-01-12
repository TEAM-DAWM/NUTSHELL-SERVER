package nutshell.server.dto.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nutshell.server.exception.IllegalArgumentException;
import nutshell.server.exception.code.IllegalArgumentErrorCode;

@Getter
@AllArgsConstructor
public enum Status {
    TODO("미완료"),
    DONE("완료"),
    IN_PROGRESS("진행 중"),
    ;

    private final String content;

    public static Status fromContent(String content) {
        for (Status status : Status.values()) {
            if (status.getContent().equals(content))
                return status;
        }
        throw new IllegalArgumentException(IllegalArgumentErrorCode.INVALID_ARGUMENTS);
    }
}
