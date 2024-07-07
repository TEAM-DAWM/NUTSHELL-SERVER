package nutshell.server.controller;

import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.dto.auth.JwtTokensDto;
import nutshell.server.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/google")
    public ResponseEntity<Void> googleLogin(
            @RequestParam final String code
    ) throws IOException {
        authService.googleLogin(code);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/re-issue")
    public ResponseEntity<JwtTokensDto> reissueToken(
        @RequestHeader("Authorization") final String refreshToken
    ){
        return ResponseEntity.ok(authService.reissueToken(refreshToken));
    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<?> logout(
            @UserId final Long userId
    ){
       authService.logout(userId);
        return ResponseEntity.noContent().build();
    }
}
