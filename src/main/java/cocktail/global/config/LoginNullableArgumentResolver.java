package cocktail.global.config;

import cocktail.application.auth.SessionUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static cocktail.api.user.SessionConst.LOGIN_USER;

public class LoginNullableArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(LoginNullable.class);
        boolean hasSessionUserType = SessionUser.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasSessionUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = req.getSession(false);
        if(session == null){
            return null;
        }
        return session.getAttribute(LOGIN_USER);
    }
}
