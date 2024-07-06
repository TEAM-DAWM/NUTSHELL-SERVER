package nutshell.server.service.token;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.Token;
import nutshell.server.dto.auth.JwtTokensDto;
import nutshell.server.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRemover tokenRemover;
    private final TokenSaver tokenSaver;
    private final TokenRetriever tokenRetriever;
    private final JwtUtil jwtUtil;

    // refreshToken으로 재발급하는 메서드 ??
    @Transactional
    public JwtTokensDto reissueToken(final String refreshToken){
        String token = refreshToken.substring("Bearer ".length());
        Long userId = tokenRetriever.findMemberIdByRefreshToken(token);
        JwtTokensDto tokensDto = jwtUtil.generateTokens(userId);
        tokenSaver.save(Token.builder().id(userId).refreshToken(tokensDto.refreshToken()).build());
        return tokensDto;
    }

    // 로그아웃 메서드 (refreshToken 삭제)
    @Transactional
    public void logout(final Long userId){
        Token refreshToken = tokenRetriever.findById(userId);
        tokenRemover.deleteToken(refreshToken);
    }
}
