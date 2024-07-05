package nutshell.server.dto.auth;

import lombok.Builder;

@Builder
public record JwtTokensDto(
        String accessToken,
        String refreshToken
) {

    public static JwtTokensDto of(String accessToken, String refreshToken) {
        return new JwtTokensDto(accessToken, refreshToken);
    }
}
