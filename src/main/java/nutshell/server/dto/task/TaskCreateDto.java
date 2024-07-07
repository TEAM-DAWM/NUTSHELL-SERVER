package nutshell.server.dto.task;
import java.time.LocalDateTime;

public record TaskCreateDto(
        String name,
       DeadLine deadLine

) {
    public record DeadLine(LocalDateTime date, String time){ }
}
