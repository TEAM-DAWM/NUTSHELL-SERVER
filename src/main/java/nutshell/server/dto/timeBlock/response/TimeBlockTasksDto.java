package nutshell.server.dto.timeBlock.response;

import lombok.Builder;
import nutshell.server.dto.googleCalender.response.GoogleSchedulesDto;

import java.util.List;

@Builder
public record TimeBlockTasksDto(
        List<TimeBlocksDto> tasks
) {
}
