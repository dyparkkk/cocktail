package cocktail.application;

import cocktail.api.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static cocktail.dto.UserDto.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class UserController {

    private final UserService userService;

    @RequestMapping(value = "/signup", method = {RequestMethod.GET,RequestMethod.POST})
    public SignUpResponseDto signUp(@Validated @RequestBody SignUpRequestDto dto){
        userService.signUp(dto);
        return new SignUpResponseDto();
    }

    @PostMapping("/login")
    public SuccessResponseDto login(@Validated @RequestBody LoginRequestDto dto,
                                    HttpServletRequest req) {
        String userId = userService.signIn(dto);

        //세션 매니저를 통해 세션 생성및 회원정보 보관
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = req.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, userId);

        return new SuccessResponseDto();
    }

    @GetMapping("/signup/userid")
    public SuccessResponseDto userIdCheck(@Validated @RequestBody UserIdCheckDto dto) {
        userService.validateDuplicateUser(dto.getUsername());
        return new SuccessResponseDto();
    }
}
