package nutshell.server.dto.task;

import java.time.LocalDateTime;

public record TaskResponse(
        int id,
        String name,
        LocalDateTime deadLing,
        String status
) {
}
