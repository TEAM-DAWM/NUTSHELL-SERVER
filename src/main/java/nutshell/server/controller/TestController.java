package nutshell.server.controller;

import jakarta.validation.Valid;
import nutshell.server.annotation.UserId;
import nutshell.server.domain.Token;
import nutshell.server.dto.auth.JwtTokensDto;
import nutshell.server.dto.test.TestDto;
import nutshell.server.dto.test.TestInput;
import nutshell.server.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import nutshell.server.exception.code.ForbiddenErrorCode;
import nutshell.server.exception.code.IllegalArgumentErrorCode;
import nutshell.server.service.token.TokenSaver;
import nutshell.server.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TestController {
    private final JwtUtil jwtUtil;
    private final TokenSaver tokenSaver;

    @GetMapping("/test/security")
    public ResponseEntity<TestDto> testSecurity(
            @UserId final Long userId,
            @Valid @RequestBody final TestInput testInput
            ) {
        return ResponseEntity.ok(TestDto.builder().content(testInput.name() + " " + userId).build());
    }

    @GetMapping("/test/success")
    public ResponseEntity<TestDto> testSuccess() {
        return ResponseEntity.ok(TestDto.builder().content("success").build());
    }

    @GetMapping("/test/token/{userId}")
    public ResponseEntity<JwtTokensDto> testToken(
            @PathVariable final Long userId
    ) {
        JwtTokensDto tokens = jwtUtil.generateTokens(userId);
        tokenSaver.save(Token.builder().id(userId).refreshToken(tokens.refreshToken()).build());
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/test/forbidden-error")
    public ResponseEntity<String> testForbiddenError() {
        throw new ForbiddenException(ForbiddenErrorCode.FORBIDDEN);
    }

    @GetMapping("/test/internal-server-error")
    public ResponseEntity<String> testInternalServerError() {
        throw new RuntimeException();
    }

    @GetMapping("/test/illegal-argument-error")
    public ResponseEntity<String> testIllegalArgumentError() {
        throw new IllegalArgumentException(IllegalArgumentErrorCode.INVALID_ARGUMENTS.getMessage());
    }
}
