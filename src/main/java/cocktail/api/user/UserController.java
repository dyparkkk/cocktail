package cocktail.api.user;

import cocktail.application.User.UserProfileService;
import cocktail.application.User.UserService;
import cocktail.application.auth.SessionUser;
import cocktail.dto.*;
import cocktail.global.config.Login;
import cocktail.global.config.LoginNullable;
import cocktail.infra.S3Uploader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    private final UserProfileService userProfileService;

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
    @ApiOperation(value = "나의 프로필 조회", notes = "로그인 필요")
    public UserPageDto getProfile(@ApiIgnore @Login SessionUser sessionUser) {
        return userProfileService.getUserPage(sessionUser.getNickname(), sessionUser);
    }

    @GetMapping("/profile/recipes")
    @ApiOperation(value = "유저가 작성한 레시피 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int",example = "0", value = "몇 페이지 (0부터 시작, default 0)"),
            @ApiImplicitParam(name = "size", dataType = "int",example = "3", value = "페이지의 요소 수(default 10)"),
            @ApiImplicitParam(name = "sort", dataType = "string",example = "star, update", value = "star, update (default create(최근 작성된 글순))")
    })
    public List<RecipeResponseDto> getUserRecipeList(@ApiIgnore @Login SessionUser sessionUser,
                                                     @ApiIgnore @PageableDefault Pageable pageable) {
        return userProfileService.getUserRecipes(sessionUser.getNickname(), pageable);
    }
}
