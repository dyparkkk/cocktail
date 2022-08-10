package cocktail.api.user;

import cocktail.application.User.FollowService;
import cocktail.application.auth.SessionUser;
import cocktail.domain.Follow;
import cocktail.domain.User;
import cocktail.dto.SuccessResponseDto;
import cocktail.global.config.Login;
import cocktail.infra.user.FollowRepository;
import cocktail.infra.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowRepository followRepository;
    private final FollowService followService;

    private final UserRepository userRepository;

    @PostMapping("/follow/{username}")
    public SuccessResponseDto follow(@Login SessionUser sessionUser, @PathVariable String username) {
       followService.follow(sessionUser,username);
        return new SuccessResponseDto();
    }

    @DeleteMapping("/follow/{username}")
    public SuccessResponseDto unFollow(@Login SessionUser sessionUser,@PathVariable String username) {
        followService.unFollow(sessionUser,username);
        return new SuccessResponseDto();
    }


    @GetMapping("/follow/{username}")
    public ResponseEntity<List<String>> getFollowList(@PathVariable String username, @Login SessionUser sessionUser){
        User user = userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);
        List<Follow> follows = followRepository.findByFromUserId(user.getId());
        List<String> followIdList = new ArrayList<String>();
        for (Follow f : follows) {
            followIdList.add(f.getFromUser().getNickname());
            followIdList.add(f.getFromUser().getProfileImgUrl());

        }

       // List<Follow> sessionFollows = followRepository.findByFromUsername(sessionUser.getUsername());

       return ResponseEntity.status(HttpStatus.ACCEPTED).body(followIdList);
   }

    @GetMapping("/follower/{username}")
    public ResponseEntity<List<String>> getFollowerList(@PathVariable String username, @Login SessionUser sessionUser){
        User user = userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);

        List<Follow> follows = followRepository.findByToUserId(user.getId());
        List<String> followerIdList = new ArrayList<String>();
        for (Follow f : follows) {
            followerIdList.add(f.getFromUser().getNickname());
            followerIdList.add(f.getFromUser().getProfileImgUrl());

        }

        // List<Follow> sessionFollows = followRepository.findByFromUsername(sessionUser.getUsername());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(followerIdList);
    }


}
