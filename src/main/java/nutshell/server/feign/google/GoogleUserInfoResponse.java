package nutshell.server.feign.google;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleUserInfoResponse(
        String sub,
        String name,
        String givenName,
        String familyName,
        String picture,
        String email,
        String emailVerified,
        String local

) {
}
