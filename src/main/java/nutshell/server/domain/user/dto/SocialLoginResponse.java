package nutshell.server.domain.user.dto;

import nutshell.server.dto.auth.JwtTokensDto;

public record SocialLoginResponse (
        Long userId,
        String userName,
        JwtTokensDto tokenResponse
){
    public static SocialLoginResponse of(Long userId, String userName, JwtTokensDto jwtTokensDto) {
        return new SocialLoginResponse(userId, userName, jwtTokensDto);
    }
}
