package cocktail.global.config;

import cocktail.api.user.SessionConst;
import cocktail.application.auth.SessionUser;
import cocktail.dto.UserDto;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static cocktail.api.user.SessionConst.*;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasSessionUserType = SessionUser.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasSessionUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest req = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = req.getSession(false);

        // login 기능이 만들어 지지 않음 ....
        if (session == null) {
            HttpSession testSession = req.getSession();
            testSession.setAttribute(TEST_USER,
                    new SessionUser(new UserDto("username@naver.com", "nickname")));
            return testSession.getAttribute(TEST_USER);
        }
        if(session.getAttribute(TEST_USER) != null){
            return session.getAttribute(TEST_USER);
        }
        // ......

        return session.getAttribute(LOGIN_USER);
    }
}
