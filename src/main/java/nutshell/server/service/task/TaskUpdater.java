package nutshell.server.service.task;

import lombok.extern.slf4j.Slf4j;
import nutshell.server.domain.Task;
import org.springframework.stereotype.Component;
import nutshell.server.dto.type.Status;
import nutshell.server.dto.task.request.TaskUpdateDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class TaskUpdater {

    // Task 설명 수정 PATCH API
    public void editDetails(
            final Task task,
            final TaskUpdateDto taskUpdateDto
    ) {
        LocalDate deadLineDate = null;
        LocalTime deadLineTime = null;

        if (taskUpdateDto.deadLine() != null) {
            deadLineDate = taskUpdateDto.deadLine().date();
            deadLineTime = taskUpdateDto.deadLine().time();
        }
        task.updateTask(taskUpdateDto.name(), taskUpdateDto.description(), deadLineDate, deadLineTime);
    }

    public void updateStatus(
            final Task task,
            final Status status
    ) {
        task.updateStatus(status);
    }

    public void updateAssignedDate(
            final Task task,
            final LocalDate assignedDate
    ) {
        task.updateAssignedDate(assignedDate);
    }
}
