package nutshell.server.dto.task.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalTime;

public record TaskCreateDto(
        @NotBlank
        String name,
        DeadLine deadLine

) {
    public record DeadLine(
            @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
            LocalDate date,
            @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
            LocalTime time
    )
    { }
}
