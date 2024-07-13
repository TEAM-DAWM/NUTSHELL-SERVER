package nutshell.server.feign.google;

import nutshell.server.constant.GoogleConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="GoogleUnlinkClient", url = GoogleConstant.GOOGLE_UNLINK_URL)
public interface GoogleUnlinkClient {
    @PostMapping
    void googleUnlink(
            @RequestParam(GoogleConstant.TOKEN) String token
    );
}
