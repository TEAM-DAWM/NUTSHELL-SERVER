package nutshell.server.domain.user.controller;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.user.dto.SocialLoginRequest;
import nutshell.server.domain.user.dto.SocialLoginResponse;
import nutshell.server.domain.user.service.UserService;
import nutshell.server.dto.common.ResponseDto;
import nutshell.server.feign.SuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<SocialLoginResponse> login(
            @RequestHeader("Authorization") String authorization,
            @RequestBody SocialLoginRequest socialLoginRequest
    ) throws IOException {
        return ResponseEntity
                .status(SuccessCode.POST_LOGIN_SUCCESS.getHttpStatus())
                .body(userService.login(SocialLoginRequest.of(socialLoginRequest.socialLoginPlatform(), authorization)));
    }

    @PostMapping("/oauth2/code/google")
    public ResponseEntity<String> getCode(
            @RequestParam final String code
    ) throws IOException {

    }


}
