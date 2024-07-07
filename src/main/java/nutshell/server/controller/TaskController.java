package nutshell.server.controller;

import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.dto.task.TaskCreateDto;
import nutshell.server.service.task.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    // Staging Area Task 생성
    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(
            @UserId final Long userId,
            @RequestBody final TaskCreateDto taskCreateDto
    ){
        taskService.createTask(userId, taskCreateDto);
        return ResponseEntity.ok().build();
    }
}
