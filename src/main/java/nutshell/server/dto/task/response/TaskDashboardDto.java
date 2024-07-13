package nutshell.server.dto.task.response;

import lombok.Builder;

@Builder
public record TaskDashboardDto(
        int completeTasks,
        double avgInprogressTasks,
        double avgDeferredRate
) {
}