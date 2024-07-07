package nutshell.server.dto.timeBlock.response;

import lombok.Builder;

import java.util.List;

@Builder
public record TimeBlocksDto(
        Long id,
        String name,
        List<TimeBlockDto> timeBlocks
) {
}
