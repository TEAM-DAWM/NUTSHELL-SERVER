package nutshell.server.dto.task.response;

import lombok.Builder;
import nutshell.server.dto.task.request.TaskCreateDto;

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
