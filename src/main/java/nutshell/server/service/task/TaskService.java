package nutshell.server.service.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nutshell.server.domain.Task;
import nutshell.server.domain.TimeBlock;
import nutshell.server.domain.User;
import nutshell.server.dto.task.request.TargetDateDto;
import nutshell.server.dto.task.request.TaskCreateDto;
import nutshell.server.dto.task.request.TaskStatusDto;
import nutshell.server.dto.task.request.TaskUpdateDto;
import nutshell.server.dto.task.response.TaskDetailDto;
import nutshell.server.dto.task.response.TasksDto;
import nutshell.server.dto.type.Status;
import nutshell.server.exception.IllegalArgumentException;
import nutshell.server.exception.code.IllegalArgumentErrorCode;
import nutshell.server.service.timeBlock.TimeBlockRetriever;
import nutshell.server.service.user.UserRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {
    private final TaskUpdater taskUpdater;
    private final TaskRetriever taskRetriever;
    private final UserRetriever userRetriever;
    private final TaskSaver taskSaver;
    private final TaskRemover taskRemover;
    private final TimeBlockRetriever timeBlockRetriever;

    @Transactional
    public void updateStatus(
            final Long userId,
            final Long taskId,
            final TaskStatusDto taskStatusDto
    ) {
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        if (taskStatusDto.targetDate() == null) {    //target area에서 staging area로 넘어갈 경우
            taskUpdater.updateAssignedDate(task, null);
            taskUpdater.updateEndDate(task, null);
            taskUpdater.updateStatus(task, Status.TODO);
        } else {
            if (taskStatusDto.status().equals("완료")) {
                taskUpdater.updateEndDate(task, taskStatusDto.targetDate());
            } else if (taskStatusDto.status().equals("미완료")) {
                if (task.getStatus().getContent().equals("완료"))
                    taskUpdater.updateEndDate(task, null);
                else if (task.getAssignedDate() == null)
                    taskUpdater.updateAssignedDate(task, taskStatusDto.targetDate());
            }
            taskUpdater.updateStatus(task, Status.valueOf(taskStatusDto.status()));
        }
    }

    // Staging Area Task 생성 API (데드라인 추가 완료)
    @Transactional
    public Task createTask(final Long userId, final TaskCreateDto taskCreateDto) {
        User user = userRetriever.findByUserId(userId);

        LocalDate deadLineDate = null;
        LocalTime deadLineTime = null;

        if (taskCreateDto.deadLine() != null) {
            deadLineDate = taskCreateDto.deadLine().date();
            deadLineTime = taskCreateDto.deadLine().time();
        }

        Task task = Task.builder()
                .user(user)
                .name(taskCreateDto.name())
                .deadLineDate(deadLineDate)
                .deadLineTime(deadLineTime)
                .build();
        return taskSaver.save(task);
    }

    public void removeTask(final Long userId, final Long taskId) {
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        taskRemover.deleteTask(task);
    }

    // Task 상세 조회 GET API (데드라인 추가 완료)
    public TaskDetailDto getTaskDetails(final Long userId, final Long taskId, final TargetDateDto targetDateDto) {
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        TimeBlock tb = targetDateDto == null ? null : timeBlockRetriever.findByTaskIdAndTargetDate(task, targetDateDto.targetDate()); // timeblock 찾아옴
        TaskDetailDto.TimeBlock timeBlock = (tb == null) ? null
                : TaskDetailDto.TimeBlock.builder().id(tb.getId()).startTime(tb.getStartTime()).endTime(tb.getEndTime()).build();

        TaskCreateDto.DeadLine deadLine = new TaskCreateDto.DeadLine(
                task.getDeadLineDate(),
                task.getDeadLineTime()
        );
        return TaskDetailDto.builder()
                .name(task.getName())
                .description(task.getDescription())
                .status(task.getStatus().getContent())
                .deadLine(deadLine)
                .timeBlock(timeBlock)
                .build();
    }

    // Task 리스트 조회 (데드라인 수정 완료)
    public TasksDto getTasks(
            final Long userId,
            final String order,
            final LocalDate targetDate
    ) {
        User user = userRetriever.findByUserId(userId);
        List<TasksDto.TaskDto> taskItems = new ArrayList<>();
        //사용자설정순 추가해야함
        if (targetDate != null) {
            List<Task> tasks = switch (order) {
                        case "recent" -> taskRetriever.findAllByUserAndAssignedDateOrderByCreatedAtDesc(user, targetDate);
                        case "old" -> taskRetriever.findAllByUserAndAssignedDateOrderByCreatedAtAsc(user, targetDate);
                        case "near" -> taskRetriever.findAllByUserAndAssignedDateOrderByTimeDiffAsc(user, targetDate);
                        case "far" -> taskRetriever.findAllByUserAndAssignedDateOrderByTimeDiffDesc(user, targetDate);
                        default -> throw new IllegalArgumentException(IllegalArgumentErrorCode.INVALID_ARGUMENTS);

                    };
        } else {
            List<Task> tasks = switch (order) {
                        case "recent" -> taskRetriever.findAllByUserAndAssignedDateIsNullOrderByCreatedAtDesc(user);
                        case "old" -> taskRetriever.findAllByUserAndAssignedDateIsNullOrderByCreatedAtAsc(user);
                        case "near" -> taskRetriever.findAllByUserAndAssignedDateIsNullOrderByTimeDiffAsc(user);
                        case "far" -> taskRetriever.findAllByUserAndAssignedDateIsNullOrderByTimeDiffDesc(user);
                        default -> throw new IllegalArgumentException(IllegalArgumentErrorCode.INVALID_ARGUMENTS);

                    };
            taskItems = tasks.stream().map(
                    task -> TasksDto.TaskDto.builder()
                            .id(task.getId())
                            .name(task.getName())
                            .status(task.getStatus().getContent())
                            .deadLine(new TaskCreateDto.DeadLine(task.getDeadLineDate(), task.getDeadLineTime()))
                            .build()
            ).toList();
        }
        return TasksDto.builder().tasks(taskItems).build();
    }

    // Task 설명 수정 PATCH API (데드라인 수정 완료)
    @Transactional
    public void updateTask(final Long userId, final Long taskId, TaskUpdateDto taskUpdateDto) {
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        taskUpdater.editDetails(task, taskUpdateDto);
    }
}
