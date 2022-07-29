package cocktail.api.user;

import cocktail.application.User.UserService;
import cocktail.application.auth.SessionUser;
import cocktail.domain.User;
import cocktail.dto.SuccessResponseDto;
import cocktail.dto.UserDto;
import cocktail.dto.UserProfileDto;
import cocktail.global.config.Login;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static cocktail.api.user.SessionConst.LOGIN_USER;
import static cocktail.dto.UserDto.LoginRequestDto;
import static cocktail.dto.UserDto.SignUpRequestDto;
import static cocktail.dto.UserDto.UserIdCheckDto;
import static cocktail.dto.UserDto.UserUpdateDto;

@Slf4j
@Api(tags = "user")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/user")
public class UserController {

    private final UserService userService;

    @RequestMapping(value = "/signup", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "회원가입", notes = "회원가입 기능.")
    public ResponseEntity<Long> signUp(@Validated @RequestBody SignUpRequestDto dto) {
        Long id = userService.signUp(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(id);

    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인 기능.")
    public ResponseEntity<String> login(@Validated @RequestBody LoginRequestDto dto,
                                    HttpServletRequest req) {
        UserDto userDto = userService.signIn(dto);
        SessionUser sessionUser = new SessionUser(userDto);

        //세션 매니저를 통해 세션 생성 및 회원정보 보관
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = req.getSession();
        session.setAttribute(LOGIN_USER, sessionUser);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userDto.getUsername());
    }

    @RequestMapping(value ="/signup/userid", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "아이디 중복 확인", notes = "아이디 중복 확인 기능.")
    public SuccessResponseDto userIdCheck(@Validated @RequestBody UserIdCheckDto dto) {
        userService.validateDuplicateUser(dto.getUsername());
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

    @GetMapping("/user")
    public List<User> listUser() {
        return userService.findAll();
    }

    @PutMapping("/update")
    public ResponseEntity<Long> userUpdate(@RequestParam Long id, @Validated @RequestBody UserUpdateDto userUpdateDto,
                                           @ApiIgnore @Login SessionUser sessionUser){
        Long userId = userService.userUpdate(id,userUpdateDto,sessionUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userId);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> profile(@RequestParam Long id,@ApiIgnore @Login SessionUser sessionUser) {
        UserProfileDto userProfileDto = userService.getProfile(id,sessionUser.getUsername());
        return ResponseEntity.status(HttpStatus.OK)
                .body(userProfileDto);
    }


    // ------- test ---------
    @GetMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인 기능.")
    public ResponseEntity<String> loginTest(@Login SessionUser sessionUser) {
        System.out.println("login : " + sessionUser);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
}
