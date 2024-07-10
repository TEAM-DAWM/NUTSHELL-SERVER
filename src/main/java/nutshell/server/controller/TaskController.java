package nutshell.server.controller;

import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.dto.task.*;
import nutshell.server.service.task.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(
            @UserId final Long userId,
            @RequestBody final TaskCreateDto taskCreateDto
    ){
        return ResponseEntity.created(URI.create(taskService.createTask(userId, taskCreateDto).getId().toString())).build();
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @UserId final Long userId,
            @PathVariable(name = "taskId") final Long taskId
    ) {
        taskService.removeTask(userId, taskId);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/tasks/{taskId}/assign")
    public ResponseEntity<Void> assignedTask(
            @UserId final Long userId,
            @PathVariable(name="taskId") final Long taskId,
            @RequestBody final TaskAssignedDto taskAssignedDto
    ){
        taskService.assignTask(userId, taskId, taskAssignedDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDto> getTask(
            @UserId final Long userId,
            @PathVariable final Long taskId,
            @RequestBody final TargetDateDto targetDateDto
    ){
        return ResponseEntity.ok(taskService.getTaskDetails(userId, taskId, targetDateDto));
    }

    @PatchMapping("/tasks/{taskId}")
    public ResponseEntity<Void> editDetail(
            @UserId final Long userId,
            @PathVariable final Long taskId,
            @RequestBody(required = false) final TaskDetailEditDto taskDetailEditDto
    ){
        taskService.editDetail(userId, taskId, taskDetailEditDto);
        return ResponseEntity.noContent().build();
    }
}
