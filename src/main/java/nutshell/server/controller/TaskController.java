package nutshell.server.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.dto.task.request.*;
import nutshell.server.dto.task.response.TaskDetailDto;
import nutshell.server.dto.task.response.TasksDto;
import nutshell.server.service.task.TaskService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;

    // task 상태 수정 PATCH API
    @PatchMapping("/tasks/{taskId}/status")
    public ResponseEntity<Void> updateStatus(
            @UserId final Long userId,
            @PathVariable final Long taskId,
            @RequestBody final TaskStatusDto taskStatusDto
    ) {
        taskService.updateStatus(userId, taskId, taskStatusDto);
        return ResponseEntity.noContent().build();
    }

    // Staging Area 에 새로운 Task 생성하는 POST API (데드라인 수정 완료)
    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(
            @UserId final Long userId,
            @Valid @RequestBody final TaskCreateDto taskCreateDto
    ){
        return ResponseEntity.created(URI.create(taskService.createTask(userId, taskCreateDto).getId().toString())).build();
    }

    //Task 삭제 DELETE API
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @UserId final Long userId,
            @PathVariable(name="taskId") final Long taskId
    ){
        taskService.removeTask(userId, taskId);
        return ResponseEntity.noContent().build();
    }

    // Task 상세조회 GET API (데드라인 수정 완료)
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDetailDto> getTask(
            @UserId final Long userId,
            @PathVariable final Long taskId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate targetDate
    ){
        TargetDateDto targetDateDto = (targetDate != null) ? new TargetDateDto(targetDate) : null;
        return ResponseEntity.ok(taskService.getTaskDetails(userId, taskId, targetDateDto));
    }

   // Task 리스트 조회 (데드라인 수정 완료)
    @GetMapping("/tasks")
    public ResponseEntity<TasksDto> getTasks(
            @UserId final Long userId,
            @RequestParam(required = false) final String order,
            @RequestParam(required = false) @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") final LocalDate targetDate
    ){
        return ResponseEntity.ok(taskService.getTasks(userId, order, targetDate));
    }

   // Task 설명 수정 PATCH API (데드라인 수정 완료)
    @PatchMapping("/tasks/{taskId}")
    public ResponseEntity<Void> updateTask(
            @UserId final Long userId,
            @PathVariable final Long taskId,
            @RequestBody(required = false) final TaskUpdateDto taskUpdateDto
    ){
        taskService.updateTask(userId, taskId, taskUpdateDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/tasks/orders")
    public ResponseEntity<Void> createOrder(
            @UserId final Long userId,
            @RequestBody final TaskOrderDto taskOrderDto
    ) {
        return ResponseEntity.created(URI.create(taskService.createOrder(userId, taskOrderDto).getId())).build();
    }
}
