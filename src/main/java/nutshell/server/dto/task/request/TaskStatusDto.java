package nutshell.server.dto.task.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record TaskStatusDto(
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate targetDate,
        String status
) {
}
