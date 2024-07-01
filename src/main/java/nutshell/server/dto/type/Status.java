package nutshell.server.dto.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    TODO("미완료"),
    DONE("완료"),
    IN_PROGRESS("진행 중"),
    DEFERRED("지연"),
    ;

    private final String content;
}
