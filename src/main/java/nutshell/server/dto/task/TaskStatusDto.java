package nutshell.server.dto.task;

import lombok.Builder;

@Builder
public record TaskStatusDto(
        String status
) {
}
