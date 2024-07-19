package nutshell.server.service.google;

import lombok.RequiredArgsConstructor;
import nutshell.server.constant.GoogleConstant;
import nutshell.server.feign.google.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleService {
    private final GoogleAuthClient googleAuthClient;
    private final GoogleInfoClient googleInfoClient;
    private final GoogleUnlinkClient googleUnlinkClient;
    private final GoogleReissueClient googleReissueClient;
    public GoogleTokenResponse getToken(
            final String code,
            final String clientId,
            final String clientSecret,
            final String redirectUri
    ) {
        return googleAuthClient.googleAuth( // 구글 인증 API 호출
                code, // 클라이언트에서 받은 인증 코드
                //구글 API에 필요한 클라이언트 정보
                clientId,
                clientSecret,
                redirectUri,
                //권한 부여 코드 타입
                GoogleConstant.AUTHORIZATION_CODE
        );
    }

    public GoogleUserInfoResponse getUserInfo(
            final String accessToken
    ) {
        return googleInfoClient.googleInfo( // 구글 사용자 정보 API를 호출
                GoogleConstant.BEARER + accessToken
        );
    }

    public void unlink(final String token) {
        googleUnlinkClient.googleUnlink(token);
    }

    public GoogleTokenResponse reissue(final GoogleReissueRequest request) throws Exception {
        return googleReissueClient.googleReissue(request);
    }
}
