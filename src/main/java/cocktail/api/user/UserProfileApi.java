package cocktail.api.user;

import cocktail.application.User.UserProfileService;
import cocktail.application.auth.SessionUser;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.UserPageDto;
import cocktail.global.config.LoginNullable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/profile")
@Api(tags = "userProfile")
public class UserProfileApi {
    private final UserProfileService userProfileService;

    @GetMapping("/{nickname}")
    @ApiOperation(value = "유저의 페이지 정보 조회(팔로우 수, 레시피 수 등)", notes = "로그인 했을 때 내가 그 유저를 팔로우 하는지 확인 가능")
    public UserPageDto getUserPage(@PathVariable String nickname,
                                   @ApiIgnore @LoginNullable SessionUser sessionUser) {
        return userProfileService.getUserPage(nickname, sessionUser);
    }

    @GetMapping("/recipes/{nickname}")
    @ApiOperation(value = "유저가 작성한 레시피 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int",example = "0", value = "몇 페이지 (0부터 시작, default 0)"),
            @ApiImplicitParam(name = "size", dataType = "int",example = "3", value = "페이지의 요소 수(default 10)"),
            @ApiImplicitParam(name = "sort", dataType = "string",example = "star, update", value = "star, update (default create(최근 작성된 글순))")
    })
    public List<RecipeResponseDto> getUserRecipeList(@PathVariable String nickname,
                                                     @ApiIgnore @PageableDefault Pageable pageable) {
        return userProfileService.getUserRecipes(nickname, pageable);
    }

    // 북마크 추가 예정
}
