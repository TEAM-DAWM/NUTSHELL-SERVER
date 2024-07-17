package nutshell.server.utils;

import nutshell.server.dto.task.request.TaskCreateDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtil {
    public static TaskCreateDto.DeadLine getDeadLine(LocalDateTime deadLine) {
        LocalDate date = deadLine != null
                ? deadLine.toLocalDate() : null;
        String time = deadLine != null
                ? deadLine.toLocalTime().toString().split(":")[0]
                + ":" + deadLine.toLocalTime().toString().split(":")[1] : null;
        return new TaskCreateDto.DeadLine(
                date,
                time
        );
    }
}
