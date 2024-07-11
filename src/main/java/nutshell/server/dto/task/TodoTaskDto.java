package nutshell.server.dto.task;

import lombok.Builder;

import java.util.List;

@Builder
public record TodoTaskDto(
        List<TaskComponentDto> tasks
) {
    @Builder
    public record TaskComponentDto(
            Long id,
            String name,
            TaskCreateDto.DeadLine deadLine
    ) { }
}
