package cocktail.api.user;

import cocktail.application.User.UserService;
import cocktail.application.auth.SessionUser;
import cocktail.dto.SuccessResponseDto;
import cocktail.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static cocktail.api.user.SessionConst.LOGIN_USER;

@Api(tags = "login & signUp")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/user")
public class LoginApi {

    private final UserService userService;

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "회원가입 기능.")
    public SuccessResponseDto signUp(@Validated @RequestBody UserDto.SignUpRequestDto dto) {
        Long id = userService.signUp(dto);
        return new SuccessResponseDto();
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인 기능.")
    public SuccessResponseDto login(@Validated @RequestBody UserDto.LoginRequestDto dto,
                                    HttpServletRequest req) {
        UserDto userDto = userService.signIn(dto);
        SessionUser sessionUser = new SessionUser(userDto);

        //세션 매니저를 통해 세션 생성 및 회원정보 보관
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = req.getSession();
        session.setAttribute(LOGIN_USER, sessionUser);

        return new SuccessResponseDto();
    }

    @PostMapping(value ="/signup/userid")
    @ApiOperation(value = "아이디 중복 확인", notes = "아이디 중복 확인 기능.")
    public SuccessResponseDto userIdCheck(@Validated @RequestBody UserDto.UserIdCheckDto dto) {
        userService.validateDuplicateUser(dto.getUsername());
        return new SuccessResponseDto();
    }

    @PostMapping(value ="/signup/nickname")
    @ApiOperation(value = "닉네임 중복 확인", notes = "닉네임 중복 확인 기능.")
    public SuccessResponseDto nicknameCheck(@Validated @RequestBody UserDto.NicknameCheckDto dto) {
        userService.validateDuplicateNickname(dto.getNickname());
        return new SuccessResponseDto();
    }


    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃", notes = "로그아웃 기능.")
    public SuccessResponseDto logout(HttpServletRequest req) {

        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new IllegalStateException("JSESSIONID를 확인할 수 없습니다.");
        }
        session.invalidate();
        return new SuccessResponseDto();
    }
}
