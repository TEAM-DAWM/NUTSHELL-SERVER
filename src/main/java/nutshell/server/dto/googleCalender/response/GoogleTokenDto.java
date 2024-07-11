package nutshell.server.dto.googleCalender.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleTokenDto(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        String refreshToken
) {
}
