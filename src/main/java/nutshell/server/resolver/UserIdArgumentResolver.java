package nutshell.server.resolver;

import lombok.NonNull;
import nutshell.server.annotation.UserId;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        boolean hasUserIdAnnotation = parameter.hasParameterAnnotation(UserId.class);
        boolean isLongType = Long.class.isAssignableFrom(parameter.getParameterType());
        return hasUserIdAnnotation && isLongType;
    }

    @Override
    public Object resolveArgument(
            @NonNull final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            @NonNull final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}