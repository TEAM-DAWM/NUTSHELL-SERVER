package nutshell.server.service.user;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.User;
import nutshell.server.exception.NotFoundException;
import nutshell.server.exception.code.NotFoundErrorCode;
import nutshell.server.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRetriever {
    private final UserRepository userRepository;

    public User findByUserId(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorCode.NOT_FOUND_USER));
    }

    public User findBySerialIdAndEmailOrGet(final String serialId, final String givenName, final String familyName, final String picture, final String email){
         return userRepository.findBySerialIdAndEmail(serialId, email).orElseGet(
                ()-> userRepository.save(User.builder().serialId(serialId).givenName(givenName).familyName(familyName).image(picture).email(email).build())

        );
    }
}
