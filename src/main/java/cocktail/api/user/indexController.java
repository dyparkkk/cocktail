package cocktail.api.user;

import cocktail.application.auth.SessionUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class indexController {

    private final HttpSession httpSession;

    @GetMapping("/")
    public String index() {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        System.out.println("user : " + user);
        return "index.html";
    }
}
