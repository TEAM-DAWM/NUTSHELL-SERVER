package nutshell.server.dto.task.response;

import lombok.Builder;
import nutshell.server.dto.task.request.TaskCreateDto;

import java.util.List;
@Builder
public record TasksDto(
        List<TaskDto> tasks
) {
    @Builder
    public record TaskDto(
            Long id,
            String name,
            TaskCreateDto.DeadLine deadLine,
            String status
    ) {
    }
}