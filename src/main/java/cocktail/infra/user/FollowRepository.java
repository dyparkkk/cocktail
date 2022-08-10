package cocktail.infra.user;

import cocktail.domain.Follow;
import cocktail.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow,Long>,FollowRepositoryCustom {

    Follow findFollowByFromUserAndToUser(User user, User toUser);

    // unFolow
    @Transactional
    Long deleteByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    // 팔로우 유무
    int countByFromUserIdAndToUserId(int fromUserId, int toUserId);

    // 팔로우 리스트 유저가 팔로우 한 사람의 목록
    List<Follow> findByFromUserNickname(String fromUser);
    List<Follow> findByFromUserId(Long fromUser);


    // 팔로워 리스트 (맞팔 체크 후 버튼 색깔 결정)
    List<Follow> findByToUserId(Long toUserId);

    // 팔로우 카운트
    Long countByFromUserId(Long fromUserId);

    // 팔로워 카운트
    Long countByToUserId(Long toUserId);

}
