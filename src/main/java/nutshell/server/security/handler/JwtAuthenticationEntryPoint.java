package nutshell.server.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import nutshell.server.dto.common.ResponseDto;
import nutshell.server.exception.code.DefaultErrorCode;
import nutshell.server.exception.code.UnAuthorizedErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {  //인증 실패시 처리
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException authException
    ) throws IOException {
        DefaultErrorCode errorCode = (DefaultErrorCode) request.getAttribute("exception");
        if (errorCode == null)
            errorCode = UnAuthorizedErrorCode.UNAUTHORIZED;
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());
        response.getWriter().write(
                objectMapper.writeValueAsString(ResponseDto.fail(errorCode))
        );
    }
}
