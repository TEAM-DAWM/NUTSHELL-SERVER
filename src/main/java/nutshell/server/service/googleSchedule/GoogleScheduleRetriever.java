package nutshell.server.service.googleSchedule;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.GoogleSchedule;
import nutshell.server.repository.GoogleScheduleRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleScheduleRetriever {
    private final GoogleScheduleRepository googleScheduleRepository;

    public GoogleSchedule findById(final Long googleCalendarId, final String categoryId) {
        String id = googleCalendarId + ":" + categoryId;
        return googleScheduleRepository.findById(id).orElse(null);
    }
    public List<GoogleSchedule> findAllByGoogleCalendarId(final Long googleCalendarId) {
        return googleScheduleRepository.findAllByGoogleCalendarId(googleCalendarId);
    }
}
