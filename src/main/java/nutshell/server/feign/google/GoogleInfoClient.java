package nutshell.server.feign.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="GoogleInfoClient", url = "https://www.googleapis.com/oauth2/v3/userinfo")
public interface GoogleInfoClient {
    @GetMapping
    GoogleUserInfoResponse googleInfo(
            @RequestHeader("Authorization") String accessToken
    );

}
