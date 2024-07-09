package nutshell.server.controller;

import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.service.googleCalender.GoogleCalenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
        URI uri = URI.create(googleCalenderService.getToken(code, userId).getId().toString());
        return ResponseEntity.created(uri).build();
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
