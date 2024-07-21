package nutshell.server.service.taskStatus;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Task;
import nutshell.server.domain.TaskStatus;
import nutshell.server.domain.TimeBlock;
import nutshell.server.dto.type.Status;
import nutshell.server.service.task.TaskRetriever;
import nutshell.server.service.task.TaskUpdater;
import nutshell.server.service.timeBlock.TimeBlockRemover;
import nutshell.server.service.timeBlock.TimeBlockRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaskStatusService {
    private final TaskUpdater taskUpdater;
    private final TaskStatusUpdater taskStatusUpdater;
    private final TaskStatusSaver taskStatusSaver;
    private final TaskRetriever taskRetriever;
    private final TimeBlockRemover timeBlockRemover;
    private final TimeBlockRetriever timeBlockRetriever;

    @Transactional
    public void scheduleTasks(TaskStatus taskStatus) {
        if (taskStatus.getStatus() == Status.TODO){
            Task task = taskRetriever.findById(taskStatus.getTask().getId());
            taskUpdater.updateStatus(task, Status.DEFERRED);
            taskUpdater.updateAssignedDate(task, null);
            taskStatusUpdater.updateStatus(taskStatus, Status.DEFERRED);
            taskStatusSaver.save(taskStatus);
            TimeBlock timeBlock = timeBlockRetriever.findByTaskStatus(taskStatus);
            if (timeBlock != null)
                timeBlockRemover.remove(timeBlock);
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
}
