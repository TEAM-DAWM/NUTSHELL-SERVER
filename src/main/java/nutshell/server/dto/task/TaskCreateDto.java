package nutshell.server.dto.task;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TaskCreateDto(
        String name,
       DeadLine deadLine

) {
    public record DeadLine(
            @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm", timezone = "Asia/Seoul")
            LocalDateTime date,
            String time){ }
}
