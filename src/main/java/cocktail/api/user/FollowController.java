package cocktail.api.user;

import cocktail.application.User.FollowService;
import cocktail.application.auth.SessionUser;
import cocktail.domain.Follow;
import cocktail.infra.user.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowRepository followRepository;
    private final FollowService followService;

    @PostMapping("follow/{toUserId}")
    public Follow followUser(@PathVariable Long toUserId, SessionUser sessionUser){
        return followService.save(sessionUser.getUsername(), toUserId);
    }

    @DeleteMapping("follow/{toUserId}")
    public void unFollowUser(@PathVariable Long toUserId, SessionUser sessionUser){
        Long id = followService.getFollowIdByFromEmailToId(sessionUser.getUsername(),toUserId);
        followRepository.deleteById(id);
    }
}
