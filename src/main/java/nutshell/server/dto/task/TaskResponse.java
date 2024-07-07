package nutshell.server.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TaskResponse(
        int id,
        String name,
        @JsonFormat(pattern = "hh-MM-dd'T'hh-mm", timezone = "Asia/Seoul")
        LocalDateTime deadLine,
        String status
) {
}
