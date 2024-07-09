package nutshell.server.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.dto.task.TaskAssignedDto;
import nutshell.server.dto.task.TaskCreateDto;
import nutshell.server.dto.task.TaskDto;
import nutshell.server.dto.task.TasksDto;
import nutshell.server.service.task.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDate;

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
            @PathVariable Long taskId
    ){
        return ResponseEntity.ok(taskService.getTaskDetails(userId, taskId));
    }

    @GetMapping("/tasks")
    public ResponseEntity<TasksDto> getTasks(
            @UserId final Long userId,
            @RequestParam(required = false) final Boolean isTotal,
            @RequestParam(required = false) final String order,
            @RequestParam(required = false) @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") final LocalDate targetDate
    ){
        return ResponseEntity.ok(taskService.getTasks(userId, isTotal, order, targetDate));
    }
}
