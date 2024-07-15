package nutshell.server.feign.google;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleReissueRequest(
        String refreshToken,
        String clientId,
        String clientSecret,
        String redirectUri,
        String grantType
) {
}
