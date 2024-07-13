package nutshell.server.feign.google;

import nutshell.server.constant.GoogleConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="GoogleReissueClient", url = GoogleConstant.GOOGLE_TOKEN_URL)
public interface GoogleReissueClient {
    @PostMapping
    GoogleTokenResponse googleReissue(
            GoogleReissueRequest googleReissueRequest
    );
}
