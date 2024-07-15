package nutshell.server.dto.task.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record TargetDateDto(
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate targetDate
) {
}
