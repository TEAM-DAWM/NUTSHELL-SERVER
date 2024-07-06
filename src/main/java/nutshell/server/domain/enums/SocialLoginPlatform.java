package nutshell.server.domain.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SocialLoginPlatform {

    GOOGLE("google"),
    LOGOUT_STATUS("logout status")
    ;

    public final String value;
}
