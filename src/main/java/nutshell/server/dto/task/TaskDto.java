package nutshell.server.dto.task;
import lombok.Builder;

@Builder
public record TaskDto(
        String name,
        String description,
        TaskCreateDto.DeadLine deadLine,
        String status
) {
}