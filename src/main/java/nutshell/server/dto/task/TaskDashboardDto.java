package nutshell.server.dto.task;

import lombok.Builder;

@Builder
public record TaskDashboardDto(
        int completeTasks,
        double avgInprogressTasks,
        double avgDeferredRate
) {
}