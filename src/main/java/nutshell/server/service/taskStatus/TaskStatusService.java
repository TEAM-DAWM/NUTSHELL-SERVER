package nutshell.server.service.taskStatus;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.domain.TaskStatus;
import nutshell.server.dto.type.Status;
import nutshell.server.service.task.TaskRetriever;
import nutshell.server.service.task.TaskUpdater;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaskStatusService {
    private final TaskStatusRetriever taskStatusRetriever;
    private final TaskUpdater taskUpdater;
    private final TaskStatusUpdater taskStatusUpdater;
    private final TaskStatusSaver taskStatusSaver;
    private final TaskRetriever taskRetriever;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void updateDeferred(){
        taskStatusRetriever.findAllByTargetDate(LocalDate.now().minusDays(1))
                .forEach(
                        taskStatus -> {
                            if (taskStatus.getStatus() == Status.TODO){
                                Task task = taskRetriever.findById(taskStatus.getTask().getId());
                                taskUpdater.updateStatus(task, Status.DEFERRED);
                                taskUpdater.updateAssignedDate(task, null);
                                taskStatusUpdater.updateStatus(taskStatus, Status.DEFERRED);
                            } else if (taskStatus.getStatus() == Status.IN_PROGRESS){
                                taskStatusSaver.save(
                                        TaskStatus.builder()
                                                .task(taskStatus.getTask())
                                                .status(Status.IN_PROGRESS)
                                                .targetDate(LocalDate.now())
                                                .build()
                                );
                            }
                        }
                );
    }
}
