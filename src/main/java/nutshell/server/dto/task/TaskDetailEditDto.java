package nutshell.server.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TaskDetailEditDto(
        String name,
        String description,
        TaskCreateDto.DeadLine deadLine,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
        LocalDateTime startTime,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
        LocalDateTime endTime
) {
}
