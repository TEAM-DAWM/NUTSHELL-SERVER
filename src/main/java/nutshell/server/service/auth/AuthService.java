package nutshell.server.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nutshell.server.domain.Token;
import nutshell.server.domain.User;
import nutshell.server.feign.google.GoogleTokenResponse;
import nutshell.server.feign.google.GoogleUserInfoResponse;
import nutshell.server.dto.auth.response.JwtTokensDto;
import nutshell.server.service.google.GoogleService;
import nutshell.server.service.token.TokenRemover;
import nutshell.server.service.token.TokenRetriever;
import nutshell.server.service.token.TokenSaver;
import nutshell.server.service.user.UserRetriever;
import nutshell.server.service.user.UserUpdater;
import nutshell.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    private final JwtUtil jwtUtil;
    private final TokenRetriever tokenRetriever;
    private final UserRetriever userRetriever;
    private final UserUpdater userUpdater;
    private final TokenSaver tokenSaver;
    private final TokenRemover tokenRemover;
    private final GoogleService googleService;

    @Transactional
    public JwtTokensDto reissueToken(final Long userId){
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
    public JwtTokensDto googleLogin(final String code) {
        GoogleTokenResponse googleTokenResponse = googleService.getToken(code, clientId, clientSecret, redirectUrl);
        GoogleUserInfoResponse googleUserInfoResponse = googleService.getUserInfo(googleTokenResponse.accessToken());
        User user = userRetriever.findBySerialIdAndEmailOrGet(
                googleUserInfoResponse.sub(),
                googleUserInfoResponse.givenName(),
                googleUserInfoResponse.familyName(),
                googleUserInfoResponse.picture(),
                googleUserInfoResponse.email()
        );
        if (user.getFamilyName() == null && user.getGivenName() == null){
            userUpdater.updateName(user, googleUserInfoResponse.givenName(), googleUserInfoResponse.familyName());
        }
        JwtTokensDto jwtTokensDto = jwtUtil.generateTokens(user.getId());
        tokenSaver.save(Token.builder().id(user.getId()).refreshToken(jwtTokensDto.refreshToken()).build());
        return jwtTokensDto;
    }
}
