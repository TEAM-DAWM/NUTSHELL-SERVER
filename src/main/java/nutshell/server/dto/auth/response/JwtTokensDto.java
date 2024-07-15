package nutshell.server.dto.auth.response;

import lombok.Builder;

@Builder
public record JwtTokensDto(
        String accessToken,
        String refreshToken
) {
}
