package nutshell.server.feign.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 구글 로그인을 통해 AccessToken을 제공받기 위한 인터페이스
@FeignClient(value = "customGoogleAuthApiClient", url = "http://localhost:8080/login/oauth2/code/google")
public interface GoogleAuthClient {
    @PostMapping
    GoogleUserInfoResponse googleAuth(
            @RequestParam(name="code") String code,
            @RequestParam(name="clientId") String clientId,
            @RequestParam(name="clientSecret") String clientSecret,
            @RequestParam(name="redirectUri") String redirectUri,
            @RequestParam(name="grantType") String grantType);
}