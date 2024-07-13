package nutshell.server.dto.task.request;

public record TaskUpdateDto(
        String name,
        String description,
        TaskCreateDto.DeadLine deadLine
) {
}
