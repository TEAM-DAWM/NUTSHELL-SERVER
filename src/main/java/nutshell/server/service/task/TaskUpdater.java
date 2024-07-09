package nutshell.server.service.task;

import lombok.extern.slf4j.Slf4j;
import nutshell.server.domain.Task;
import nutshell.server.dto.task.TaskDetailEditDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TaskUpdater {

    public void updateAssignedTask(
            final Task task,
            final LocalDate targetDate
    ) {
        task.updateAssignedDate(targetDate);
    }

    public void editDetails(
            final Task task,
            final TaskDetailEditDto taskDetailEditDto
    ) {
        LocalDate date = taskDetailEditDto.deadLine().date();
        String time = taskDetailEditDto.deadLine().time();

        String dateTimeString = date.toString() + "T" + time;
        LocalDateTime deadLine = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        task.updateTask(taskDetailEditDto.name(), taskDetailEditDto.description(), deadLine);
    }

}
