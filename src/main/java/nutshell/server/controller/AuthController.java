package nutshell.server.controller;

import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.dto.auth.JwtTokensDto;
import nutshell.server.service.token.TokenService;
import nutshell.server.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;

    @GetMapping("/login/google")
    public ResponseEntity<Void> googleLogin(
            @RequestParam String code
    ) throws IOException {
        authService.googleLogin(code);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/re-issue")
    public ResponseEntity<JwtTokensDto> reissueToken(
        @RequestHeader("Authorization") String refreshToken
    ){
        return ResponseEntity.ok(tokenService.reissueToken(refreshToken));
    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<?> logout(@UserId Long userId){ //토큰넣으면 유저아이디 자동으로 찾아와줌
        tokenService.logout(userId);
        return ResponseEntity.noContent().build();
    }
}
