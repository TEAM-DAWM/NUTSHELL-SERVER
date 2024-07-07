package nutshell.server.service.auth;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nutshell.server.domain.Token;
import nutshell.server.domain.User;
import nutshell.server.feign.google.GoogleTokenResponse;
import nutshell.server.feign.google.GoogleUserInfoResponse;
import nutshell.server.dto.auth.JwtTokensDto;
import nutshell.server.feign.google.GoogleAuthClient;
import nutshell.server.feign.google.GoogleInfoClient;
import nutshell.server.service.token.TokenRemover;
import nutshell.server.service.token.TokenRetriever;
import nutshell.server.service.token.TokenSaver;
import nutshell.server.service.user.UserRetriever;
import nutshell.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUrl;

    private final GoogleAuthClient googleAuthClient;
    private final GoogleInfoClient googleInfoClient;
    private final JwtUtil jwtUtil;
    private final TokenRetriever tokenRetriever;
    private final UserRetriever userRetriever;
    private final TokenSaver tokenSaver;
    private final TokenRemover tokenRemover;

    @Transactional
    public JwtTokensDto reissueToken(final String refreshToken){
        String token = refreshToken.substring("Bearer ".length());
        Long userId = tokenRetriever.findMemberIdByRefreshToken(token);
        JwtTokensDto tokensDto = jwtUtil.generateTokens(userId);
        tokenSaver.save(Token.builder().id(userId).refreshToken(tokensDto.refreshToken()).build());
        return tokensDto;
    }

    @Transactional
    public void logout(final Long userId){
        Token refreshToken = tokenRetriever.findById(userId);
        tokenRemover.deleteToken(refreshToken);
    }

    @Transactional
    public JwtTokensDto googleLogin(final String code)
            throws IOException{
        log.info("{}", code);
        GoogleTokenResponse googleTokenResponse = googleAuthClient.googleAuth( // 구글 인증 API 호출
               code, // 클라이언트에서 받은 인증 코드
                //구글 API에 필요한 클라이언트 정보
                clientId,
                clientSecret,
                redirectUrl,
                //권한 부여 코드 타입
                "authorization_code"
        );
        GoogleUserInfoResponse googleUserInfoResponse = googleInfoClient.googleInfo( // 구글 사용자 정보 API를 호출
                "Bearer " + googleTokenResponse.accessToken());

        User user = userRetriever.findBySerialIdAndEmailOrGet(googleUserInfoResponse.sub(), googleUserInfoResponse.name(), googleUserInfoResponse.email());
        JwtTokensDto jwtTokensDto = jwtUtil.generateTokens(user.getId());
        tokenSaver.save(Token.builder().id(user.getId()).refreshToken(jwtTokensDto.refreshToken()).build());
        return jwtTokensDto;
    }
}
