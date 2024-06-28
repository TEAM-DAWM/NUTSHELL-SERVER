package nutshell.server.security.config;

import nutshell.server.constant.AuthConstant;
import nutshell.server.security.filter.JwtAuthenticationFilter;
import nutshell.server.security.filter.JwtExceptionFilter;
import nutshell.server.security.handler.JwtAuthenticationEntryPoint;
import nutshell.server.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // CSRF 보호 비 활성화
                .csrf(AbstractHttpConfigurer::disable)
                // HTTP Basic 인증 비 활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                // 폼 로그인 비 활성화
                .formLogin(AbstractHttpConfigurer::disable)
                // 세션 관리 설정: 세션을 사용 하지 않음 (Stateless)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 요청 인증 설정
                .authorizeHttpRequests(registry ->
                        registry
                                // 화이트 리스트 경로는 인증 없이 접근 허용
                                .requestMatchers(AuthConstant.AUTH_WHITELIST).permitAll()
                                // 그 외의 모든 요청은 인증 필요
                                .anyRequest().authenticated()
                )
                // 예외 처리 설정: 인증 실패 시 커스텀 엔트리 포인트 사용
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                // 필터 체인에 JwtAuthenticationFilter 추가: UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class)
                // 필터 체인에 JwtExceptionFilter 추가: JwtAuthenticationFilter 앞에 추가
                .addFilterBefore(
                        new JwtExceptionFilter(),
                        JwtAuthenticationFilter.class)
                .getOrBuild();
    }

}