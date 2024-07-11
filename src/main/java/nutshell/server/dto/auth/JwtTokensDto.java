package nutshell.server.dto.auth;

import lombok.Builder;

@Builder
public record JwtTokensDto(
        String accessToken,
        String refreshToken
) {
}
