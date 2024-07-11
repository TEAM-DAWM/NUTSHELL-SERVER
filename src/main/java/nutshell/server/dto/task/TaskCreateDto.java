package nutshell.server.dto.task;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record TaskCreateDto(
        String name,
       DeadLine deadLine

) {
    public record DeadLine(
            @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
            LocalDate date,
            String time){ }
}
