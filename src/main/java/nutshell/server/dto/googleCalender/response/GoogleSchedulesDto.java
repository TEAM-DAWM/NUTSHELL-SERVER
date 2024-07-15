package nutshell.server.dto.googleCalender.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record GoogleSchedulesDto(
        String id,
        String name,
        String color,
        List<GoogleScheduleDto> schedules
) {
    @Builder
    public record GoogleScheduleDto(
            String name,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime startTime,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
            LocalDateTime endTime,
            Boolean allDay
    ) {
    }
}