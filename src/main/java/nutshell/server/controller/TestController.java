package nutshell.server.controller;

import nutshell.server.domain.Token;
import nutshell.server.dto.auth.response.JwtTokensDto;
import lombok.RequiredArgsConstructor;
import nutshell.server.service.token.TokenSaver;
import nutshell.server.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {
    private final JwtUtil jwtUtil;
    private final TokenSaver tokenSaver;

    @GetMapping("/test/token/{userId}")
    public ResponseEntity<JwtTokensDto> testToken(
            @PathVariable final Long userId
    ) {
        JwtTokensDto tokens = jwtUtil.generateTokens(userId);
        tokenSaver.save(Token.builder().id(userId).refreshToken(tokens.refreshToken()).build());
        return ResponseEntity.ok(tokens);
    }
}
