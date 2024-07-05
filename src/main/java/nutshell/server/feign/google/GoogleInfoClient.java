package nutshell.server.feign.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

// 발급 받은 accessToken으로 구글 API에 원하는 요청을 보낼 클라이언트
@FeignClient(name="GoogleInfoClient", url = "https://www.googleapis.com/oauth2/v3/userinfo")
public interface GoogleInfoClient {

    @GetMapping
    GoogleUserInfoResponse googleInfo(
            @RequestHeader("Authorization") String accessToken
    );
}
