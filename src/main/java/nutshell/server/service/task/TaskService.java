package nutshell.server.service.task;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.domain.User;
import nutshell.server.dto.task.TaskCreateDto;
import nutshell.server.dto.task.TaskResponse;
import nutshell.server.service.user.UserRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserRetriever userRetriever;
    private final TaskSaver taskSaver;
    private final TaskRetriever taskRetriever;
    private final TaskRemover taskRemover;

    @Transactional
    public Task createTask(final Long userId, final TaskCreateDto taskCreateDto){
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
        return taskSaver.save(task);
    }

    @Transactional
    public void removeTask(final Long userId, final Long taskId) {
        Task task = taskRetriever.findTaskByTaskId(taskId);
        taskRemover.deleteTask(task);
    }

    @Transactional
    public void assignTask(final Long userId, final Long taskId){
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findTaskByTaskId(taskId);
        task.updateAssignedDate(LocalDate.now());
    }

}
