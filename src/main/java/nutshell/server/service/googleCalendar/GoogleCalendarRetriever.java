package nutshell.server.service.googleCalendar;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.GoogleCalendar;
import nutshell.server.domain.User;
import nutshell.server.exception.NotFoundException;
import nutshell.server.exception.code.NotFoundErrorCode;
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
    public boolean existsByUserAndEmail(final User user, final String email) {
        return googleCalendarRepository.existsByUserAndEmail(user, email);
    }

    public List<GoogleCalendar> findAllByUser(final User user) {
        return googleCalendarRepository.findAllByUser(user);
    }
}
