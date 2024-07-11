package nutshell.server.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nutshell.server.constant.AuthConstant;
import nutshell.server.dto.auth.JwtTokensDto;
import nutshell.server.exception.UnAuthorizedException;
import nutshell.server.exception.code.UnAuthorizedErrorCode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil implements InitializingBean {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-expire-period}")
    @Getter
    private Integer accessTokenExpirePeriod;

    @Value("${jwt.refresh-token-expire-period}")
    @Getter
    private Integer refreshTokenExpirePeriod;

    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtTokensDto generateTokens(final Long id) {
        return JwtTokensDto.builder()
                .accessToken(generateToken(id, accessTokenExpirePeriod))
                .refreshToken(generateToken(id, refreshTokenExpirePeriod))
                .build();
    }

    private String generateToken(final Long id, final Integer expirePeriod) {
        Claims claims = Jwts.claims();
        claims.put(AuthConstant.USER_ID_CLAIM_NAME, id);

        return Jwts.builder()
                .setHeaderParam(Header.JWT_TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirePeriod))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims getTokenBody(final String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static Object checkPrincipal(final Object principal) {
        if (AuthConstant.ANONYMOUS_USER.equals(principal)) {
            throw new UnAuthorizedException(UnAuthorizedErrorCode.UNAUTHORIZED);
        }
        return principal;
    }

}