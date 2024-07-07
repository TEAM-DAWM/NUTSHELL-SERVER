package nutshell.server.controller;

import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.service.googleCalender.GoogleCalenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GoogleCalenderController {

    private final GoogleCalenderService googleCalenderService;

    @GetMapping("/google/calenders/tokens")
    public ResponseEntity<Void> getTokens(
            @UserId final Long userId,
            @RequestParam final String code
    ) {
        googleCalenderService.getToken(code, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/google/calenders/{googleCalenderId}")
    public ResponseEntity<Void> deleteCalender(
            @UserId final Long userId,
            @PathVariable final Long googleCalenderId
    ) {
        googleCalenderService.unlink(userId, googleCalenderId);
        return ResponseEntity.noContent().build();
    }
}
