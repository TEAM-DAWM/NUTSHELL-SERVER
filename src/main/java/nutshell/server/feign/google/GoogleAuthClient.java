package nutshell.server.feign.google;

import nutshell.server.constant.GoogleConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "customGoogleAuthApiClient", url = GoogleConstant.GOOGLE_TOKEN_URL)
public interface GoogleAuthClient {
    @PostMapping
    GoogleTokenResponse googleAuth(
            @RequestParam(GoogleConstant.CODE) final String code,
            @RequestParam(GoogleConstant.CLIENT_ID) final String clientId,
            @RequestParam(GoogleConstant.CLIENT_SECRET) final String clientSecret,
            @RequestParam(GoogleConstant.REDIRECT_URI) final String redirectUri,
            @RequestParam(GoogleConstant.GRANT_TYPE) final String grantType);
}