package cocktail.application.User;

import cocktail.application.auth.SessionUser;
import cocktail.domain.Follow;
import cocktail.domain.User;
import cocktail.infra.user.FollowRepository;
import cocktail.infra.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional
    public long getFollowIdByFromEmailToId(String email, Long toId){

        User formUser = userRepository.findByUsername(email)
                .orElseThrow(IllegalArgumentException::new);

        User toUser = userRepository.findById(toId).
                orElseThrow(IllegalArgumentException::new);

        Follow follow = followRepository.findFollowByFromUserAndToUser(formUser,toUser);

        if(follow != null)
            return follow.getId();
        else
            return -1;
    }

    @Transactional
    public Follow save(SessionUser sessionUser, String toUsername){
        User formUser = userRepository.findByUsername(sessionUser.getUsername())
                .orElseThrow(IllegalArgumentException::new);

        User toUser = userRepository.findByUsername(toUsername).
                orElseThrow(IllegalArgumentException::new);

        return followRepository.save(Follow.builder()
                .fromUser(formUser)
                .toUser(toUser)
                .build());
    }
}
