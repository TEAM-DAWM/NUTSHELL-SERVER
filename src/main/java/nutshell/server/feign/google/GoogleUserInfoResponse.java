package nutshell.server.feign.google;

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
