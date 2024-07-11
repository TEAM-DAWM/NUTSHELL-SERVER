package nutshell.server.service.task;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.domain.TaskStatus;
import nutshell.server.domain.User;
import nutshell.server.dto.task.TaskStatusDto;
import nutshell.server.dto.type.Status;
import nutshell.server.exception.BusinessException;
import nutshell.server.exception.IllegalArgumentException;
import nutshell.server.exception.code.BusinessErrorCode;
import nutshell.server.exception.code.IllegalArgumentErrorCode;
import nutshell.server.service.taskStatus.*;
import nutshell.server.service.user.UserRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskUpdater taskUpdater;
    private final TaskRetriever taskRetriever;
    private final UserRetriever userRetriever;
    private final TaskStatusRetriever taskStatusRetriever;
    private final TaskStatusSaver taskStatusSaver;
    private final TaskStatusRemover taskStatusRemover;
    private final TaskStatusUpdater taskStatusUpdater;

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
                if (taskStatusDto.targetDate().isBefore(LocalDate.now()))
                    throw new BusinessException(BusinessErrorCode.BUSINESS_TODAY);
            } else if (status != Status.DONE){
                throw new IllegalArgumentException(IllegalArgumentErrorCode.INVALID_ARGUMENTS);
            }
            taskUpdater.updateAssignedDate(task, taskStatusDto.targetDate());
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
            taskUpdater.updateStatus(task, status);
        }
    }
}
