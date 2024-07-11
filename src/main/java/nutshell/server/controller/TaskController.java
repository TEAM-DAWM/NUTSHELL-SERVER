package nutshell.server.controller;

import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.dto.task.TaskCreateDto;
import nutshell.server.service.task.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    // Staging Area 에 새로운 Task 생성하는 POST API
    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(
            @UserId final Long userId,
            @RequestBody final TaskCreateDto taskCreateDto
    ){
        return ResponseEntity.created(URI.create(taskService.createTask(userId, taskCreateDto).getId().toString())).build();
    }

}
