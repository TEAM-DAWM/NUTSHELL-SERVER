package nutshell.server.security.info;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthentication extends UsernamePasswordAuthenticationToken {

    public UserAuthentication(
            final Object principal,
            final Object credentials,
            final Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static UserAuthentication createUserAuthentication(final Long userId) {
        return new UserAuthentication(userId, null, null);
    }
}