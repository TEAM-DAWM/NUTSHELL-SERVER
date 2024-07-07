package nutshell.server.dto.timeBlock.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TimeBlockCreateDto(
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate targetDate,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
        LocalDateTime startTime,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
        LocalDateTime endTime
) {
}
