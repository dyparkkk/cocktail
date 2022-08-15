package cocktail.api.user;

import cocktail.application.User.FollowService;
import cocktail.application.auth.SessionUser;
import cocktail.dto.FollowDto;
import cocktail.dto.FollowReqDto;
import cocktail.global.config.Login;
import cocktail.global.config.LoginNullable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/follow")
@Api(tags = "follow")
public class FollowController {

    private final FollowService followService;

    @PostMapping
    @ApiOperation(value = "follow 하기", notes = "로그인 필수")
    public String followUser(@RequestParam String nickname,
                             @ApiIgnore @Login SessionUser sessionUser){
        followService.follow(sessionUser, nickname);
        return "ok";
    }

    @DeleteMapping
    @ApiOperation(value = "unFollow 하기", notes = "로그인 필수")
    public String unFollowUser(@RequestParam String nickname,
                             @ApiIgnore @Login SessionUser sessionUser){
        followService.unFollow(sessionUser , nickname);
        return "ok";
    }

    @GetMapping("/follower")
    @ApiOperation(value = "user A의 팔로워 목록 조회", notes = "로그인 필수는 아님, 로그인시 내가 팔로워한 유저 확인 가능")
    public List<FollowDto> getFollowerList(@RequestParam String nickname,
                                  @ApiIgnore @LoginNullable SessionUser sessionUser) {
        return followService.findFollowerList(nickname, sessionUser);
    }

    @GetMapping("/following")
    @ApiOperation(value = "user A의 팔로잉 목록 조회", notes = "로그인 필수는 아님, 로그인시 내가 팔로워한 유저 확인 가능")
    public List<FollowDto> getFollowingList(@RequestParam String nickname,
                                           @ApiIgnore @LoginNullable SessionUser sessionUser) {
        return followService.findFollowingList(nickname, sessionUser);
    }

}
