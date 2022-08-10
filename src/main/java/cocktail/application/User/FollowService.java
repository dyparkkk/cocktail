package cocktail.application.User;

import cocktail.application.auth.SessionUser;
import cocktail.domain.Follow;
import cocktail.domain.User;
import cocktail.infra.user.FollowRepository;
import cocktail.infra.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional
    public String follow(SessionUser sessionUser,String username){
        User fromUser =  userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(IllegalArgumentException::new);
        Optional<User> user = userRepository.findByUsername(username);
        User toUser = user.get();

        Follow follow = new Follow(fromUser,toUser);

        followRepository.save(follow);
        return "ok";
    }

    @Transactional
    public String unFollow(SessionUser sessionUser,String username){
        User fromUser =userRepository.findByUsername(sessionUser.getUsername()).orElseThrow(IllegalArgumentException::new);
        Optional<User> user = userRepository.findByUsername(username);
        User toUser = user.get();

        followRepository.deleteByFromUserIdAndToUserId(fromUser.getId(), toUser.getId());

        List<Follow> follows = followRepository.findAll();
        return "ok";
    }

    public List<Follow> findByToUserId(Long toUserId) {
        return followRepository.findByToUserId(toUserId);
    }

    public List<String> followList(String username, SessionUser sessionUser) {
        //User user = userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);
        List<Follow> followList = followRepository.findByFromUserNickname(username);
        List<String> followIdList = new ArrayList<String>();
        for (Follow f : followList) {
            followIdList.add(f.getFromUser().getNickname());

        }
        return followIdList;
    }




}
