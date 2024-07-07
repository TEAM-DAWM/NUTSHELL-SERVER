package nutshell.server.service.googleCalender;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.GoogleCalender;
import nutshell.server.domain.User;
import nutshell.server.exception.NotFoundException;
import nutshell.server.exception.code.NotFoundErrorCode;
import nutshell.server.repository.GoogleCalenderRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleCalenderRetriever {
    private final GoogleCalenderRepository googleCalenderRepository;
    public GoogleCalender findByIdAndUser(final Long id, final User user) {
        return googleCalenderRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new NotFoundException(NotFoundErrorCode.NOT_FOUND_GOOGLE_CALENDER)
        );
    }

    public List<GoogleCalender> findAllByUser(final User user) {
        return googleCalenderRepository.findAllByUser(user);
    }
}
