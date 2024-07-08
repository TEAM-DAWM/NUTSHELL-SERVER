package nutshell.server.dto.task;

import java.time.LocalDate;

public record TaskAssignedDto(
        LocalDate targetDate
) {
}
