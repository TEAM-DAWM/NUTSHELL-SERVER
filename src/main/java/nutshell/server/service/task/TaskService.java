package nutshell.server.service.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nutshell.server.domain.Task;
import nutshell.server.domain.TaskStatus;
import nutshell.server.domain.TimeBlock;
import nutshell.server.domain.User;
import nutshell.server.dto.task.*;
import nutshell.server.dto.type.Status;
import nutshell.server.exception.BusinessException;
import nutshell.server.exception.IllegalArgumentException;
import nutshell.server.exception.code.BusinessErrorCode;
import nutshell.server.exception.code.IllegalArgumentErrorCode;
import nutshell.server.service.taskStatus.*;
import nutshell.server.service.timeBlock.TimeBlockRetriever;
import nutshell.server.service.user.UserRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskUpdater taskUpdater;
    private final TaskRetriever taskRetriever;
    private final UserRetriever userRetriever;
    private final TaskStatusRetriever taskStatusRetriever;
    private final TaskStatusSaver taskStatusSaver;
    private final TaskStatusRemover taskStatusRemover;
    private final TaskStatusUpdater taskStatusUpdater;
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
        Status status = Status.fromContent(taskStatusDto.status());
        if (task.getAssignedDate() == null) {   //staging area에서 할당될 때
            if (status == Status.TODO) {
                if (taskStatusDto.targetDate().isBefore(LocalDate.now())) // 할당 하려는 날짜가 now 보다 이전이면 예외
                    throw new BusinessException(BusinessErrorCode.BUSINESS_TODAY);
            } else if (status != Status.DONE) { // 완료랑 미완료만 staging area에서 가질 수 있으므로, 아니면 예외
                throw new IllegalArgumentException(IllegalArgumentErrorCode.INVALID_ARGUMENTS);
            }

            // targetDate에 해당하는 TaskStatus가 이미 존재하는지 확인하고, 존재하면 예외
            if (taskStatusRetriever.existsByTaskAndTargetDate(task, taskStatusDto.targetDate()))
                throw new BusinessException(BusinessErrorCode.BUSINESS_DUP_DAY);

            // task의 assignedDate를 targetDate로 업데이트하고, 새로운 TaskStatus를 저장
            taskUpdater.updateAssignedDate(task, taskStatusDto.targetDate());
            taskStatusSaver.save(
                    TaskStatus.builder()
                            .task(task)
                            .status(status)
                            .targetDate(taskStatusDto.targetDate())
                            .build()
            );
        } else {    //target date area에서 수정될 때
            if ((status == Status.DONE || status == Status.IN_PROGRESS)
                    && taskStatusDto.targetDate().isBefore(LocalDate.now())
            ) {    //완료 = targetDate 이후 삭제, 진행 중 = targetDate이후 값 변경
                taskStatusRemover.removeAll(
                        taskStatusRetriever.findAllByTaskAndTargetDateGreaterThan(
                                task, taskStatusDto.targetDate()
                        )
                );
                if (status == Status.IN_PROGRESS) {
                    List<TaskStatus> taskStatuses = new ArrayList<>();
                    for (LocalDate date = taskStatusDto.targetDate().plusDays(1);
                         date.isBefore(LocalDate.now().plusDays(1));
                         date = date.plusDays(1)
                    ) {
                        taskStatuses.add(
                                TaskStatus.builder()
                                        .task(task)
                                        .status(status)
                                        .targetDate(date)
                                        .build()
                        );
                    }
                    taskStatusSaver.saveAll(taskStatuses);
                }
            } else if (status == Status.TODO) {
                taskStatusRemover.removeAll(
                        taskStatusRetriever.findAllByTask(task, taskStatusDto.targetDate())
                );
                if (taskStatusDto.targetDate().isBefore(LocalDate.now())) {
                    status = Status.DEFERRED;
                    taskUpdater.updateAssignedDate(task, null);
                }
            }
            TaskStatus taskStatus = taskStatusRetriever.findByTaskAndTargetDate(
                    task, taskStatusDto.targetDate()
            );
            taskStatusUpdater.updateStatus(taskStatus, status);
        }
        taskUpdater.updateStatus(task, status);
    }

    @Transactional
    public Task createTask(final Long userId, final TaskCreateDto taskCreateDto) {
        User user = userRetriever.findByUserId(userId);

        LocalDateTime deadLine = null;
        if (taskCreateDto.deadLine() != null) {
            LocalDate date = taskCreateDto.deadLine().date();

            String[] timeParts = taskCreateDto.deadLine().time().split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            LocalTime time = LocalTime.of(hour, minute);

            deadLine = LocalDateTime.of(date, time);
        }

        Task task = Task.builder()
                .user(user)
                .name(taskCreateDto.name())
                .deadLine(deadLine)
                .build();
        return taskSaver.save(task);
    }

    public void removeTask(final Long userId, final Long taskId) {
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        taskRemover.deleteTask(task);
    }

    public TaskDto getTaskDetails(final Long userId, final Long taskId, final TargetDateDto targetDateDto) {
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);

        LocalDate date = task.getDeadLine() != null
                ? task.getDeadLine().toLocalDate() : null;
        String time = task.getDeadLine() != null
                ? task.getDeadLine().getHour() + ":" + task.getDeadLine().getMinute() : null;

        TimeBlock tb = timeBlockRetriever.findByTaskIdAndTargetDate(task, targetDateDto.targetDate()); //timeblock 찾아옴
        TaskDto.TimeBlock timeBlock = (tb == null) ? null
                : TaskDto.TimeBlock.builder().id(tb.getId()).startTime(tb.getStartTime()).endTime(tb.getEndTime()).build();

        return TaskDto.builder()
                .name(task.getName())
                .description(task.getDescription())
                .status(taskStatusRetriever.findByTaskAndTargetDate(task, targetDateDto.targetDate()).getStatus().getContent())
                .deadLine(new TaskCreateDto.DeadLine(date, time))
                .timeBlock(timeBlock)
                .build();
    }

    public TasksDto getTasks(
            final Long userId,
            final Boolean isTotal,
            final String order,
            final LocalDate targetDate
    ) {
        User user = userRetriever.findByUserId(userId);
        List<TasksDto.TaskItemDto> taskItems;
        if (targetDate != null) {
            List<TaskStatus> taskStatuses = new ArrayList<>();
            taskStatuses.addAll(taskStatusRetriever.findAllByTargetDateAndStatusDesc(user, targetDate, Status.IN_PROGRESS));
            taskStatuses.addAll(taskStatusRetriever.findAllByTargetDateAndStatusDesc(user, targetDate, Status.TODO));
            taskStatuses.addAll(taskStatusRetriever.findAllByTargetDateAndStatusDesc(user, targetDate, Status.DONE));
            taskItems = taskStatuses
                    .stream().map(
                            taskStatus -> TasksDto.TaskItemDto.builder()
                                    .id(taskStatus.getTask().getId())
                                    .name(taskStatus.getTask().getName())
                                    .hasDescription(taskStatus.getTask().getDescription() != null)
                                    .status(taskStatus.getStatus().getContent())
                                    .deadLine(new TaskCreateDto.DeadLine(
                                            taskStatus.getTask().getDeadLine().toLocalDate(),
                                            taskStatus.getTask().getDeadLine().getHour() + ":" + taskStatus.getTask().getDeadLine().getMinute()
                                    )).build()
                    ).toList();
        } else {
            List<Task> tasks = order == null ? taskRetriever.findAllByUserAndAssignedDateIsNullOrderByCreatedAtDesc(user)
                    :
                    switch (order) {
                        case "recent" -> taskRetriever.findAllByUserAndAssignedDateIsNullOrderByCreatedAtDesc(user);
                        case "old" -> taskRetriever.findAllByUserAndAssignedDateIsNullOrderByCreatedAtAsc(user);
                        case "near" -> taskRetriever.findAllByUserAndAssignedDateIsNullOrderByTimeDiffAsc(user);
                        case "far" -> taskRetriever.findAllByUserAndAssignedDateIsNullOrderByTimeDiffDesc(user);
                        default -> throw new IllegalArgumentException(IllegalArgumentErrorCode.INVALID_ARGUMENTS);

                    };
            tasks = isTotal ? tasks : tasks.stream().filter(task -> task.getStatus().equals(Status.DEFERRED)).toList();
            taskItems = tasks.stream().map(
                    task -> TasksDto.TaskItemDto.builder()
                            .id(task.getId())
                            .name(task.getName())
                            .hasDescription(task.getDescription() != null)
                            .status(task.getStatus().getContent())
                            .deadLine(new TaskCreateDto.DeadLine(
                                    task.getDeadLine().toLocalDate(),
                                    task.getDeadLine().getHour() + ":" + task.getDeadLine().getMinute()
                            )).build()
            ).toList();
        }
        return TasksDto.builder().tasks(taskItems).build();
    }

    @Transactional
    public void editDetail(final Long userId, final Long taskId, TaskDetailEditDto taskDetailEditDto) {
        User user = userRetriever.findByUserId(userId);
        Task task = taskRetriever.findByUserAndId(user, taskId);
        taskUpdater.editDetails(task, taskDetailEditDto);
    }

    // 마지막 API
    public TaskDashboardDto getDashboard(
            final Long userId,
            final LocalDate startDate,
            final LocalDate endDate,
            final Boolean isMonth,
            final Boolean isWeek
    ) {
        User user = userRetriever.findByUserId(userId);

        int completeTasks = 0; // 완료된 일들
        int avgInprogressTasks = 0; // 진행 중인 일들
        int avgDeferredTasks = 0; // 지연된 일들
        int assignedTasks = 0; // 할당된 일들

        double avgDeferredRate = 0; // 지연카운트 % 기간 내 할당 된 일 개수
        double avgInprogressDate = 0; // 기간 내 평균 진행 중인 일

        if (startDate != null && endDate != null) {
            completeTasks = taskStatusRetriever.countAllTasksInPeriod(user, startDate, endDate, Status.DONE); //완료된 할 일 갯수
            avgInprogressTasks = taskStatusRetriever.countAllTasksInPeriod(user, startDate, endDate, Status.IN_PROGRESS);
            avgDeferredTasks = taskStatusRetriever.countAllTasksInPeriod(user, startDate, endDate, Status.DEFERRED);
            assignedTasks = taskRetriever.countAllAssignedTasksInPeriod(userId, startDate, endDate);

            avgInprogressDate = Math.round(((double) avgInprogressTasks / ChronoUnit.DAYS.between(startDate, endDate)) * 10) / 10.0;
            if (assignedTasks != 0) { // 할당된 작업이 있는 경우에만 계산
                avgDeferredRate = Math.round(((double) avgDeferredTasks / assignedTasks) * 1000) / 10.0;
            }

        } else if (isMonth) { // 지난 1달
            LocalDate now = LocalDate.now().plusDays(1);
            LocalDate past = now.minusDays(30);

            completeTasks = taskStatusRetriever.countAllTasksInPeriod(user, past, now, Status.DONE);
            avgInprogressTasks = taskStatusRetriever.countAllTasksInPeriod(user, past, now, Status.IN_PROGRESS);
            avgDeferredTasks = taskStatusRetriever.countAllTasksInPeriod(user, past, now, Status.DEFERRED);
            assignedTasks = taskRetriever.countAllAssignedTasksInPeriod(userId, past, now);

            avgInprogressDate = Math.round(((double) avgInprogressTasks / ChronoUnit.DAYS.between(past, now)) * 10);
            if (assignedTasks != 0) { // 할당된 작업이 있는 경우에만 계산
                avgDeferredRate = Math.round(((double) avgDeferredTasks / assignedTasks) * 1000) / 10.0;
            }

        } else if (isWeek) { // 지난 1주일
            LocalDate nowDay = LocalDate.now().plusDays(1);
            LocalDate pastDay = nowDay.minusDays(7);

            completeTasks = taskStatusRetriever.countAllTasksInPeriod(user, pastDay, nowDay, Status.DONE);
            avgInprogressTasks = taskStatusRetriever.countAllTasksInPeriod(user, pastDay, nowDay, Status.IN_PROGRESS);
            avgDeferredTasks = taskStatusRetriever.countAllTasksInPeriod(user, pastDay, nowDay, Status.DEFERRED);
            assignedTasks = taskRetriever.countAllAssignedTasksInPeriod(userId, pastDay, nowDay);

            avgInprogressDate = Math.round(((double) avgInprogressTasks / ChronoUnit.DAYS.between(pastDay, nowDay)) * 10) / 10.0;
            if (assignedTasks != 0) { // 할당된 작업이 있는 경우에만 계산
                avgDeferredRate = Math.round(((double) avgDeferredTasks / assignedTasks) * 1000) / 10.0;
            }

        } else {
            throw new BusinessException(BusinessErrorCode.BUSINESS_PERIOD);
        }

        return TaskDashboardDto.builder()
                .completeTasks(completeTasks)
                .avgInprogressTasks(avgInprogressDate)
                .avgDeferredRate(avgDeferredRate)
                .build();
    }
}
