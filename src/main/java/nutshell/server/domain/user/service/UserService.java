package nutshell.server.domain.user.service;


import lombok.RequiredArgsConstructor;
import nutshell.server.domain.user.dto.SocialLoginRequest;
import nutshell.server.domain.user.dto.SocialLoginResponse;
import nutshell.server.domain.user.repository.UserRepository;
import nutshell.server.dto.auth.JwtTokensDto;
import nutshell.server.feign.google.GoogleAuthClient;
import nutshell.server.feign.google.GoogleInfoClient;
import nutshell.server.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    private final UserRepository userRepository;
    private final GoogleAuthClient googleAuthClient;
    private final GoogleInfoClient googleInfoClient;
    private final JwtUtil jwtUtil;

    // accessToken 받아오기 위해
    @Transactional
    public SocialLoginResponse login(SocialLoginRequest socialLoginRequest)
            throws IOException {
        return null;
    }

    // 유저 정보 받아온걸로 구글에 로그인
    @Transactional
    public SocialLoginResponse googleLogin(SocialLoginRequest socialLoginRequest)
            throws IOException{
        return null;
    }

    // accessToken 재발급 메서드
    @Transactional
    public JwtTokensDto reissueAccessToken(String refreshToken){
        return null;
    }

    // 로그아웃 메서드 (refreshToken 삭제)
    @Transactional
    public void logout(Long userId){
        jwtUtil.deleteRefreshToken(userId);
    }
}
