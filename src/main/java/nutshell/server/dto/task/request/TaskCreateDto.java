package nutshell.server.dto.task.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record TaskCreateDto(
        @NotNull
        String name,
       DeadLine deadLine

) {
    public record DeadLine(
            @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
            LocalDate date,
            @JsonFormat(pattern = "HH:mm:ss", timezone = "Asia/Seoul")
            LocalTime time
    )
    { }
}
