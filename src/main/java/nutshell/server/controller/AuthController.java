package nutshell.server.controller;

import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.dto.auth.response.JwtTokensDto;
import nutshell.server.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login/google")
    public ResponseEntity<JwtTokensDto> googleLogin(
            @RequestParam final String code
    ) {
        return ResponseEntity.ok(authService.googleLogin(code));
    }

    @PostMapping("/auth/re-issue")
    public ResponseEntity<JwtTokensDto> reissueToken(
        @UserId Long userId
    ){
        return ResponseEntity.ok(authService.reissueToken(userId));
    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<Void> logout(
            @UserId final Long userId
    ){
       authService.logout(userId);
        return ResponseEntity.noContent().build();
    }
}
