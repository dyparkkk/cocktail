package cocktail.api.user;

import cocktail.application.User.FollowService;
import cocktail.application.auth.SessionUser;
import cocktail.domain.Follow;
import cocktail.global.config.Login;
import cocktail.infra.user.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowRepository followRepository;
    private final FollowService followService;

    @PostMapping("follow/{toUsername}")
    public Follow followUser(@PathVariable String toUsername, @ApiIgnore @Login SessionUser sessionUser){
        return followService.save(sessionUser, toUsername);
    }

    @DeleteMapping("follow/{toUserId}")
    public void unFollowUser(@PathVariable Long toUserId, SessionUser sessionUser){
        Long id = followService.getFollowIdByFromEmailToId(sessionUser.getUsername(),toUserId);
        followRepository.deleteById(id);
    }
}
