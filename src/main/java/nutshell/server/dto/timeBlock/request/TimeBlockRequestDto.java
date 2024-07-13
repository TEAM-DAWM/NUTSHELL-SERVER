package nutshell.server.dto.timeBlock.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TimeBlockRequestDto(
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
        LocalDateTime startTime,
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
        LocalDateTime endTime
) {
}
