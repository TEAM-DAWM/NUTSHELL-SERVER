package nutshell.server.controller;

import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.dto.task.TaskStatusDto;
import nutshell.server.service.task.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;

    @PatchMapping("/tasks/{taskId}/status")
    public ResponseEntity<Void> updateStatus(
            @UserId final Long userId,
            @PathVariable final Long taskId,
            @RequestBody final TaskStatusDto taskStatusDto
    ) {
        taskService.updateStatus(userId, taskId, taskStatusDto);
        return ResponseEntity.noContent().build();
    }
}
