package nutshell.server.service.googleCalendar;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.GoogleCalendar;
import nutshell.server.domain.User;
import nutshell.server.exception.NotFoundException;
import nutshell.server.exception.code.NotFoundErrorCode;
import nutshell.server.feign.google.GoogleTokenResponse;
import nutshell.server.feign.google.GoogleUserInfoResponse;
import nutshell.server.repository.GoogleCalendarRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleCalendarRetriever {
    private final GoogleCalendarRepository googleCalendarRepository;
    public GoogleCalendar findByIdAndUser(final Long id, final User user) {
        return googleCalendarRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new NotFoundException(NotFoundErrorCode.NOT_FOUND_GOOGLE_CALENDER)
        );
    }
    public GoogleCalendar findByUserAndEmail(
            final User user,
            final GoogleTokenResponse googleTokenResponse,
            final GoogleUserInfoResponse googleUserInfoResponse) {
        return googleCalendarRepository.findByUserAndEmail(user, googleUserInfoResponse.email()).orElseGet(
                () -> googleCalendarRepository.save(GoogleCalendar.builder()
                        .user(user)
                        .accessToken(googleTokenResponse.accessToken())
                        .refreshToken(googleTokenResponse.refreshToken())
                        .email(googleUserInfoResponse.email())
                        .build()
                )
        );
    }

    public List<GoogleCalendar> findAllByUser(final User user) {
        return googleCalendarRepository.findAllByUser(user);
    }
}
