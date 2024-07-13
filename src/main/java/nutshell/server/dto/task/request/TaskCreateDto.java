package nutshell.server.dto.task.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskCreateDto(
        @NotNull
        String name,
       DeadLine deadLine

) {
    public record DeadLine(
            @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
            LocalDate date,
            String time)
    {
    }
}
