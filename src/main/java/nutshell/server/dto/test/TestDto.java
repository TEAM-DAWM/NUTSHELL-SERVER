package nutshell.server.dto.test;

import lombok.Builder;

@Builder
public record TestDto(
        String content
) {
}