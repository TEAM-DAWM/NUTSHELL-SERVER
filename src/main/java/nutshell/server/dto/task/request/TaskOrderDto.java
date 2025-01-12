package nutshell.server.dto.task.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;

public record TaskOrderDto(
        @NotBlank
        Boolean type,
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate targetDate,
        @NotBlank
        List<Long> taskList
) {
}
