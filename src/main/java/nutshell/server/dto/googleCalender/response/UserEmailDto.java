package nutshell.server.dto.googleCalender.response;

import lombok.Builder;

@Builder
public record UserEmailDto(
        Long id,
        String email
) {
}
