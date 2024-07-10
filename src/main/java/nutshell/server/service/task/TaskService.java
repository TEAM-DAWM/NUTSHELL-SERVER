package nutshell.server.service.task;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Defer;
import nutshell.server.domain.Task;
import nutshell.server.domain.User;
import nutshell.server.dto.task.*;
import nutshell.server.dto.type.Status;
import nutshell.server.exception.NotFoundException;
import nutshell.server.exception.code.NotFoundErrorCode;
import nutshell.server.service.defer.DeferSaver;
import nutshell.server.service.user.UserRetriever;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserRetriever userRetriever;
    private final TaskSaver taskSaver;
    private final TaskRetriever taskRetriever;
    private final TaskRemover taskRemover;
    private final TaskUpdater taskUpdater;
    private final DeferSaver deferSaver;

    @Transactional
    public Task createTask(final Long userId, final TaskCreateDto taskCreateDto){
        User user = userRetriever.findByUserId(userId);

        LocalDateTime deadLine = taskCreateDto.deadLine() != null
                ? taskCreateDto.deadLine().date().atTime(
                        Integer.parseInt(taskCreateDto.deadLine().time().split(":")[0]),
                        Integer.parseInt(taskCreateDto.deadLine().time().split(":")[1])
                )
                : null;

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
        Task task = taskRetriever.findByUserAndId(user, taskId);
        taskRemover.deleteTask(task);
    }


    @Transactional
    public void assignTask(final Long userId, final Long taskId, final TaskAssignedDto taskAssignedDto){
        if (taskAssignedDto == null) {
            throw new IllegalArgumentException("TaskAssignedDto is null");
        }
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        LocalDate targetDate = taskAssignedDto.targetDate();
        taskUpdater.updateAssignedTask(task, targetDate);
    }
  
    public TaskDto getTaskDetails(final Long userId, final Long taskId){
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        LocalDate date = task.getDeadLine() != null ? task.getDeadLine().toLocalDate() : null;
        String time = task.getDeadLine() != null ? task.getDeadLine().getHour() + ":" + task.getDeadLine().getMinute() : null;

        return TaskDto.builder().name(task.getName())
                .description(task.getDescription())
                .deadLine(new TaskCreateDto.DeadLine(date, time))
                .status(task.getStatus().getContent())
                .build();
    }

    @Transactional
    public void editStatus(final Long userId, final Long taskId, final TaskStatusDto taskStatusDto){
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        Status status = Status.fromContent(taskStatusDto.status());
        taskUpdater.updateStatus(task, status);
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void statusSchedule(){
        taskRetriever.findAllByStatusAndAssignedDateLessThan()
                .forEach(
                        this::makeDefer
                );
    }

    @Transactional
    public void makeDefer(final Task task){
        taskUpdater.updateStatus(task, Status.DEFERRED);
        Defer defer = Defer.builder().task(task).build();
        deferSaver.save(defer);
    }

    @Transactional
    public void editDetail(final Long userId, final Long taskId, TaskDetailEditDto taskDetailEditDto){
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        taskUpdater.editDetails(task, taskDetailEditDto);
    }

    public TodoTaskDto getTasksOfType(final Long userId, final String type){
        User user = userRetriever.findByUserId(userId);
        List<Task> tasks = new ArrayList<>();

        if (type.equals("upcoming")){
            tasks = taskRetriever.findAllUpcomingTasksByUserWitAssignedStatus(userId);
        } else if (type.equals("inprogress")) {
            tasks = taskRetriever.findAllInprogressTasksByUserWithStatus(userId);
        } else if (type.equals("deferred")) {
            tasks = taskRetriever.findAllDeferredTasksByUserWithStatus(userId);
        } else {
            throw new NotFoundException(NotFoundErrorCode.NOT_FOUND_TASK_TYPE);
        }
        return TodoTaskDto.builder()
                .tasks(
                        tasks.stream().map( task -> TodoTaskDto.TaskComponentDto.builder()
                                .id(task.getId())
                                .name(task.getName())
                                .deadLine(
                                        new TaskCreateDto.DeadLine(
                                                task.getDeadLine().toLocalDate(),
                                                task.getDeadLine().getHour() + ":" +
                                                        task.getDeadLine().getMinute()
                                        )
                                ).build()
                        ).toList()
                ).build();
    }
}
