package nutshell.server.dto;

import lombok.Builder;
import nutshell.server.dto.auth.JwtTokensDto;

@Builder
public record SocialLoginResponse (
        Long userId,
        String userName,
        JwtTokensDto tokenResponse
){
    public static SocialLoginResponse of(Long userId, String userName, JwtTokensDto jwtTokensDto) {
        return new SocialLoginResponse(userId, userName, jwtTokensDto);
    }
}
