package nutshell.server.service.task;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.domain.User;
import nutshell.server.dto.task.TaskCreateDto;
import nutshell.server.dto.task.TaskResponse;
import nutshell.server.service.user.UserRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserRetriever userRetriever;
    private final TaskSaver taskSaver;

    @Transactional
    public void createTask(Long userId, TaskCreateDto taskCreateDto){
        User user = userRetriever.findByUserId(userId);

        LocalDateTime deadLine = taskCreateDto.deadLine() != null
                ? taskCreateDto.deadLine().date()
                .withHour(Integer.parseInt(taskCreateDto.deadLine().time().split(":")[0]))
                .withMinute(Integer.parseInt(taskCreateDto.deadLine().time().split(":")[1]))
                : null; //null 체크 안하면 에러남!

        Task task = Task.builder()
                .user(user)
                .name(taskCreateDto.name())
                .deadLine(deadLine)
                .build();
        taskSaver.save(task);
    }
}
