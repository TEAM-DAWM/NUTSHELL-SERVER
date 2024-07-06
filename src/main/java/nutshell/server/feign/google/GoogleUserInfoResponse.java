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
    public static GoogleUserInfoResponse of(String sub, String name, String givenName, String familyName, String picture,
                                            String email, String emailVerified, String local) {
        return new GoogleUserInfoResponse(sub, name, givenName, familyName, picture, email, emailVerified, local);
    }
}
