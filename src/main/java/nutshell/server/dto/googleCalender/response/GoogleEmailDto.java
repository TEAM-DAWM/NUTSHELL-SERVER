package nutshell.server.dto.googleCalender.response;

import lombok.Builder;

@Builder
public record GoogleEmailDto(
        Long id,
        String email
) {
}
