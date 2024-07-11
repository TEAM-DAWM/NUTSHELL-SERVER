package nutshell.server.service.task;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.domain.User;
import nutshell.server.dto.task.TaskAssignedDto;
import nutshell.server.dto.task.TaskCreateDto;
import nutshell.server.dto.task.TaskDto;
import nutshell.server.dto.task.TasksDto;
import nutshell.server.dto.type.Status;
import nutshell.server.exception.IllegalArgumentException;
import nutshell.server.exception.code.IllegalArgumentErrorCode;
import nutshell.server.service.user.UserRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserRetriever userRetriever;
    private final TaskSaver taskSaver;
    private final TaskRetriever taskRetriever;
    private final TaskRemover taskRemover;
    private final TaskUpdater taskUpdater;

    @Transactional
    public Task createTask(final Long userId, final TaskCreateDto taskCreateDto){
        User user = userRetriever.findByUserId(userId);

        LocalDateTime deadLine = taskCreateDto.deadLine() != null
                ? taskCreateDto.deadLine().date().atTime(
                        Integer.parseInt(taskCreateDto.deadLine().time().split(":")[0]),
                        Integer.parseInt(taskCreateDto.deadLine().time().split(":")[1])
                )
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
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findTaskByTaskId(taskId);
        taskRemover.deleteTask(task);
    }


    @Transactional
    public void assignTask(final Long userId, final Long taskId, final TaskAssignedDto taskAssignedDto){
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findTaskByTaskId(taskId);
        LocalDate targetDate = taskAssignedDto.targetDate();
        taskUpdater.updateAssignedTask(task, targetDate);
    }
  
    public TaskDto getTaskDetails(final Long userId, final Long taskId){
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findTaskByTaskId(taskId);
        LocalDate date = task.getDeadLine() != null ? task.getDeadLine().toLocalDate() : null;
        String time = task.getDeadLine() != null ? task.getDeadLine().getHour() + ":" + task.getDeadLine().getMinute() : null;

        return TaskDto.builder().name(task.getName())
                .description(task.getDescription())
                .deadLine(new TaskCreateDto.DeadLine(date, time))
                .status(task.getStatus().getContent())
                .build();
    }


    public TasksDto getTasks(
            final Long userId,
            final Boolean isTotal,
            final String order,
            final LocalDate targetDate
    ){
        User user = userRetriever.findByUserId(userId);
        List<Task> tasks = new ArrayList<>();
        if(targetDate == null) {
            tasks = order == null ? taskRetriever.findAllByUserAndAssignedDateIsNullOrderByCreatedAtDesc(user)
            :
                switch (order) {
                    case "recent" -> taskRetriever.findAllByUserAndAssignedDateIsNullOrderByCreatedAtDesc(user);
                    case "old" -> taskRetriever.findAllByUserAndAssignedDateIsNullOrderByCreatedAtAsc(user);
                    case "near" -> taskRetriever.findAllByUserAndAssignedDateIsNullOrderByTimeDiffAsc(user);
                    case "far" -> taskRetriever.findAllByUserAndAssignedDateIsNullOrderByTimeDiffDesc(user);
                    default -> throw new IllegalArgumentException(IllegalArgumentErrorCode.INVALID_ARGUMENTS);

            };
            tasks = isTotal ? tasks : tasks.stream().filter(task -> task.getStatus().equals(Status.DEFERRED)).toList();

        }else{
            tasks.addAll(taskRetriever.findAllByUserAndStatusOrderByUpdatedStatusAtDescAssignedDateDesc(user, Status.IN_PROGRESS, targetDate));
            tasks.addAll(taskRetriever.findAllByUserAndStatusOrderByUpdatedStatusAtDescAssignedDateDesc(user, Status.TODO, targetDate));
            tasks.addAll(taskRetriever.findAllByUserAndStatusOrderByUpdatedStatusAtDescAssignedDateDesc(user, Status.DONE, targetDate));
        }
        return TasksDto.builder()
                .tasks(
                        tasks.stream().map(task -> TasksDto.TaskItemDto.builder()
                                .id(task.getId())
                                .name(task.getName())
                                .hasDescription(task.getDescription() != null)
                                .deadLine(
                                        new TaskCreateDto.DeadLine(
                                                task.getDeadLine().toLocalDate(),
                                                task.getDeadLine().getHour() + ":" + task.getDeadLine().getMinute()
                                        )
                                )
                                .status(task.getStatus().getContent())
                                .build()
                        ).toList()
                ).build();
    }
}
