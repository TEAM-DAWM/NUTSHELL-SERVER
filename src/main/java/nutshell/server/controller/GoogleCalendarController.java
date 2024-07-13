package nutshell.server.controller;

import lombok.RequiredArgsConstructor;
import nutshell.server.annotation.UserId;
import nutshell.server.dto.googleCalender.response.GoogleCategoriesDto;
import nutshell.server.service.googleCalendar.GoogleCalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GoogleCalendarController {

    private final GoogleCalendarService googleCalendarService;

    @PostMapping("/google/calendars")
    public ResponseEntity<Void> getTokens(
            @UserId final Long userId,
            @RequestParam final String code
    ) {
        URI uri = URI.create(googleCalendarService.getToken(code, userId).getId().toString());
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/google/calendars/{googleCalendarId}")
    public ResponseEntity<Void> deleteCalender(
            @UserId final Long userId,
            @PathVariable final Long googleCalendarId
    ) {
        googleCalendarService.unlink(userId, googleCalendarId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/google/calendars/categories")
    public ResponseEntity<GoogleCategoriesDto> getCategories(
            @UserId final Long userId
    ) {
        return ResponseEntity.ok(googleCalendarService.getCategories(userId));
    }
}
