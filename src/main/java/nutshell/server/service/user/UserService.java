package nutshell.server.service.user;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.User;
import nutshell.server.dto.googleCalender.response.GoogleEmailDto;
import nutshell.server.dto.user.response.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRetriever userRetriever;

    @Transactional(readOnly = true)
    public UserDto getUser(final Long userId){
        User user = userRetriever.findById(userId);
        return UserDto.builder()
                .givenName(user.getGivenName())
                .familyName(user.getFamilyName())
                .image(user.getImage())
                .email(user.getEmail())
                .googleCalenders(
                        user.getGoogleCalendars().stream()
                                .map(googleCalender -> GoogleEmailDto.builder()
                                        .id(googleCalender.getId())
                                        .email(googleCalender.getEmail())
                                        .build()
                                )
                                .toList()
                ).build();
    }
}
