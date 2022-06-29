package cocktail.api.user;

import cocktail.application.auth.SessionUser;
import cocktail.global.config.Login;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@Api(tags = "user")
@RequiredArgsConstructor
public class indexController {

    private final HttpSession httpSession;

    @GetMapping("/")
    @ApiOperation(value = "소셜로그인", notes = "소셜로그인 기능.")
    public String index() {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        System.out.println("user : " + user);
        return "index.html";
    }

    @GetMapping("/v2")
    @ApiOperation(value = "소셜로그인", notes = "소셜로그인 기능.")
    public String indexV2(@Login SessionUser sessionUser) {
        System.out.println("user : " + sessionUser);
        return "index.html";
    }

}
