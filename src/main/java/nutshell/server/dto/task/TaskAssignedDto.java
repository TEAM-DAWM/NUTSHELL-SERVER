package nutshell.server.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

public record TaskAssignedDto(
        @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Seoul")
        LocalDate targetDate
) {
}
