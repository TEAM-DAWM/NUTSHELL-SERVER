package nutshell.server.service.user;

import nutshell.server.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserUpdater {

    public void updateName(final User user, final String givenName, final String familyName) {
        user.updateName(givenName, familyName);
    }
}
