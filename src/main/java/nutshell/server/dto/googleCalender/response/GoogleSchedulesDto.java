package nutshell.server.dto.googleCalender.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GoogleSchedulesDto(
        String id,
        String name,
        String color,
        List<GoogleScheduleDto> schedules
) {
}
