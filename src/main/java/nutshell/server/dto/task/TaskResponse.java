package nutshell.server.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskResponse(
        Long id,
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH-mm", timezone = "Asia/Seoul")
        LocalDateTime deadLine,
        String status
) {
}
