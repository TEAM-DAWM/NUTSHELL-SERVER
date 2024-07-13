package nutshell.server.service.task;

import nutshell.server.domain.Task;
import org.springframework.stereotype.Component;
import nutshell.server.dto.type.Status;
import nutshell.server.dto.task.request.TaskUpdateDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TaskUpdater {

    public void editDetails(
            final Task task,
            final TaskUpdateDto taskUpdateDto
    ) {
        LocalDate date = taskUpdateDto.deadLine().date();
        String time = taskUpdateDto.deadLine().time();

        String dateTimeString = date.toString() + "T" + time;
        LocalDateTime deadLine = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        task.updateTask(taskUpdateDto.name(), taskUpdateDto.description(), deadLine);
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
