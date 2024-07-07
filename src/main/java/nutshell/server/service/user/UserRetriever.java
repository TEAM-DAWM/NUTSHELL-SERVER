package nutshell.server.service.user;

import lombok.RequiredArgsConstructor;
import nutshell.server.domain.User;
import nutshell.server.repository.UserRepository;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserRetriever {
    private final UserRepository userRepository;
    public User findBySerialIdAndEmailOrGet(final String serialId, final String name, final String email){
         return userRepository.findBySerialIdAndEmail(serialId, email).orElseGet(
                ()-> userRepository.save(User.builder().serialId(serialId).name(name).email(email).build())
        );
    }
}
