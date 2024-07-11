package nutshell.server.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.dto.task.TargetDateDto;
import nutshell.server.dto.task.TaskDto;
import nutshell.server.dto.task.TaskStatusDto;
import nutshell.server.dto.task.TaskCreateDto;
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

    @PatchMapping("/tasks/{taskId}/status")
    public ResponseEntity<Void> updateStatus(
            @UserId final Long userId,
            @PathVariable final Long taskId,
            @RequestBody final TaskStatusDto taskStatusDto
    ) {
        taskService.updateStatus(userId, taskId, taskStatusDto);
        return ResponseEntity.noContent().build();
    }

    // Staging Area 에 새로운 Task 생성하는 POST API
    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(
            @UserId final Long userId,
            @RequestBody final TaskCreateDto taskCreateDto
    ){
        return ResponseEntity.created(URI.create(taskService.createTask(userId, taskCreateDto).getId().toString())).build();
    }

    //Task 삭제 API
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @UserId final Long userId,
            @PathVariable(name="taskId") final Long taskId
    ){
        taskService.removeTask(userId, taskId);
        return ResponseEntity.noContent().build();
    }

    // Task 상세조회 GET API
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDto> getTask(
        @UserId final Long userId,
        @PathVariable final Long taskId,
        @RequestBody final TargetDateDto targetDateDto
    ){
        return ResponseEntity.ok(taskService.getTaskDetails(userId, taskId, targetDateDto));
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
