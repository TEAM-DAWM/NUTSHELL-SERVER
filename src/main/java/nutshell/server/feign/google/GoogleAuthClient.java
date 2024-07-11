package nutshell.server.feign.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "customGoogleAuthApiClient", url = "https://oauth2.googleapis.com/token")
public interface GoogleAuthClient {
    @PostMapping
    GoogleTokenResponse googleAuth(
            @RequestParam(name="code") String code,
            @RequestParam(name="clientId") String clientId,
            @RequestParam(name="clientSecret") String clientSecret,
            @RequestParam(name="redirectUri") String redirectUri,
            @RequestParam(name="grantType") String grantType);
}