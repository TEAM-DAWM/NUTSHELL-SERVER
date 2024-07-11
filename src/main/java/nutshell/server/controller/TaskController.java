package nutshell.server.controller;

import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.service.task.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    //Task 삭제 API
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @UserId final Long userId,
            @PathVariable(name="taskId") final Long taskId
    ){
        taskService.removeTask(userId, taskId);
        return ResponseEntity.noContent().build();
    }
}
