package cocktail.api;

import cocktail.dto.SessionDto;
import cocktail.dto.SessionDto.SessionInfoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@Api(tags = "session")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/session")
public class SessionController {

    @GetMapping("/session")
    @ApiOperation(value = "세션", notes = "JSESSIONID 확인")
    public SessionInfoResponse sessionInfo(HttpServletRequest req) {

        HttpSession session = req.getSession(false);

        SessionDto user = (SessionDto) session.getAttribute("user");
        if (user == null) {
            throw new IllegalStateException("JSESSIONID를 확인할 수 없습니다.");
        }

        return new SessionInfoResponse((String) session.getAttribute("user"),
                session.getId(),
                new Date(session.getCreationTime()),
                new Date(session.getLastAccessedTime()));
    }
}
