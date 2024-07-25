package nutshell.server.security.filter;

import lombok.NonNull;
import nutshell.server.constant.AuthConstant;
import nutshell.server.security.info.UserAuthentication;
import nutshell.server.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain
    ) throws ServletException, IOException {
        final String token = getJwtFromRequest(request);    //헤더에서 토큰 찾기
        if (StringUtils.hasText(token)) {   //토큰 있으면 토큰으로부터 유저 정보 가져와서 인증 객체 생성
            log.info("====================token: {}", token);
            Claims claims = jwtUtil.getTokenBody(token);
            log.info("====================claim: {}", claims);
            Long userId = claims.get(AuthConstant.USER_ID_CLAIM_NAME, Long.class);
            UserAuthentication authentication = UserAuthentication.createUserAuthentication(userId);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);    //다음 필터로 넘기기
    }

    private String getJwtFromRequest(final HttpServletRequest request) {
       String bearerToken = request.getHeader(AuthConstant.AUTHORIZATION_HEADER);
       if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AuthConstant.BEARER_PREFIX)) {
           return bearerToken.substring(AuthConstant.BEARER_PREFIX.length());
       }
       return null;
    }
}
