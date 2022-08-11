package cocktail.infra.user;

import cocktail.domain.user.Follow;
import cocktail.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Long>,FollowRepositoryCustom {
    Follow findFollowByFromUserAndToUser(User loginUser, User user);
}
