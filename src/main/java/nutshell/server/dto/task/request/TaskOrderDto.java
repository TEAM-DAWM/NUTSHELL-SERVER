package nutshell.server.dto.task.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record TaskOrderDto(
        Boolean type,
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate targetDate,
        List<Long> taskList
) {
}
