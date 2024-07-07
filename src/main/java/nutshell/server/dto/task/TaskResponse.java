package nutshell.server.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskResponse(
        Long id,
        String name,
        @JsonFormat(pattern = "hh-MM-dd'T'hh-mm", timezone = "Asia/Seoul")
        LocalDateTime deadLine,
        String status
) {
}
