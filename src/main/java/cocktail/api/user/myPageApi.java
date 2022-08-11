package cocktail.api.user;

import cocktail.application.User.UserMyPageService;
import cocktail.application.auth.SessionUser;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.UserPageDto;
import cocktail.global.config.LoginNullable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/myPage")
@Api(tags = "myPage")
public class myPageApi {
    private final UserMyPageService userMyPageService;

    @GetMapping
    @ApiOperation(value = "유저의 페이지 정보 조회(팔로우 수, 레시피 수 등)", notes = "로그인 했을 때 내가 그 유저를 팔로우 하는지 확인 가능")
    @ApiImplicitParam(name = "username", dataType = "string",example = "username@naver.com",value = "userId")
    public UserPageDto getUserPage(@RequestParam String username,
                                   @ApiIgnore @LoginNullable SessionUser sessionUser) {
        return userMyPageService.getUserPage(username, sessionUser);
    }

    @GetMapping("/recipes")
    @ApiOperation(value = "유저가 작성한 레시피 목록 조회")
    @ApiImplicitParam(name = "username", dataType = "string",example = "username@naver.com",value = "userId")
    public List<RecipeResponseDto> getUserRecipeList(@RequestParam String username) {
        return userMyPageService.getUserRecipes(username);
    }

    // 북마크 추가 예정
}
