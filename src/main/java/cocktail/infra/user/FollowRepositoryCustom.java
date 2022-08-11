package cocktail.infra.user;

import cocktail.domain.user.Follow;
import cocktail.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface FollowRepositoryCustom {

    long unFollow(User fromUser , User toUser);

    List<Follow> findFollowingList(User fromUser);

    List<Follow> findFollowerList(User fromUser);

    Optional<Follow> findByUsers(User fromUser, User toUser);

}
