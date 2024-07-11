package nutshell.server.dto.task;

public record TaskDetailEditDto(
        String name,
        String description,
        TaskCreateDto.DeadLine deadLine
) {
}
