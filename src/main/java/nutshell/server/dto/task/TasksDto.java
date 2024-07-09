package nutshell.server.dto.task;

import lombok.Builder;

import java.util.List;

@Builder
public record TasksDto(
        List<TaskItemDto> tasks
) {
    @Builder
    public record TaskItemDto(
            Long id,
            String name,
            TaskCreateDto.DeadLine deadLine,
            Boolean hasDescription,
            String status
    ) {
    }
}
