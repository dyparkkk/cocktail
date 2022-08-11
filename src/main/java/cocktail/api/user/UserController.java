package cocktail.api.user;

import cocktail.application.User.UserService;
import cocktail.application.auth.SessionUser;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.SuccessResponseDto;
import cocktail.dto.UserDto;
import cocktail.dto.UserProfileDto;
import cocktail.global.config.Login;
import cocktail.infra.S3Uploader;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static cocktail.api.user.SessionConst.LOGIN_USER;
import static cocktail.dto.UserDto.LoginRequestDto;
import static cocktail.dto.UserDto.NicknameCheckDto;
import static cocktail.dto.UserDto.SignUpRequestDto;
import static cocktail.dto.UserDto.UserIdCheckDto;
import static cocktail.dto.UserDto.UserUpdateDto;

@Slf4j
@Api(tags = "user")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/user")
public class UserController {

    private final UserService userService;
    private final S3Uploader s3Uploader;

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", notes = "회원가입 기능.")
    public SuccessResponseDto signUp(@Validated @RequestBody SignUpRequestDto dto) {
        Long id = userService.signUp(dto);
        return new SuccessResponseDto();
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인 기능.")
    public SuccessResponseDto login(@Validated @RequestBody LoginRequestDto dto,
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
    public SuccessResponseDto userIdCheck(@Validated @RequestBody UserIdCheckDto dto) {
        userService.validateDuplicateUser(dto.getUsername());
        return new SuccessResponseDto();
    }

    @PostMapping(value ="/signup/nickname")
    @ApiOperation(value = "닉네임 중복 확인", notes = "닉네임 중복 확인 기능.")
    public SuccessResponseDto nicknameCheck(@Validated @RequestBody NicknameCheckDto dto) {
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

    @PutMapping("/update")
    @ApiOperation(value = "수정", notes = "로그인 필요")
    public SuccessResponseDto userUpdate(@Validated @RequestBody UserUpdateDto userUpdateDto,
                                           @ApiIgnore @Login SessionUser sessionUser){
        String userId = userService.userUpdate(userUpdateDto,sessionUser);
        return new SuccessResponseDto();
    }

    @PostMapping("/profile/upload")
    @ApiOperation(value = "profile image", notes = "프로필 이미지 호출")
    public String profileUpload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile);
    }

    @GetMapping("/profile")
    @ApiOperation(value = "프로필 조회", notes = "로그인 필요")
    public ResponseEntity<UserProfileDto> profile(@RequestBody UserIdCheckDto dto,
                                                  @ApiIgnore @Login SessionUser sessionUser) {
        String username = dto.getUsername();
        UserProfileDto userProfileDto = userService.getProfile(username,sessionUser);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userProfileDto);
    }

    @GetMapping("/recipes")
    @ApiOperation(value = "내가 작성한 레시피 조회", notes = "로그인 필요")
    public List<RecipeResponseDto> findMyRecipes(@ApiIgnore @Login SessionUser sessionUser) {
        return userService.findMyRecipes(sessionUser);
    }

}
