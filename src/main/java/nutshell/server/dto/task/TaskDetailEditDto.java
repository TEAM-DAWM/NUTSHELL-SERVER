package nutshell.server.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TaskDetailEditDto(
        String name,
        String description,
        TaskCreateDto.DeadLine deadLine
) {
}
