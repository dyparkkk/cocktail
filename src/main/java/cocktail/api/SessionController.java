package cocktail.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

import static cocktail.dto.SessionDto.*;
import static cocktail.api.SessionConst.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class SessionController {

    @GetMapping("/session")
    public SessionInfoResponse sessionInfo(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new IllegalStateException("JSESSIONID를 확인할 수 없습니다.");
        }

        return new SessionInfoResponse((String) session.getAttribute(LOGIN_MEMBER),
                session.getId(),
                new Date( session.getCreationTime()),
                new Date(session.getLastAccessedTime()));
    }
}
