package cocktail.infra.user;

import cocktail.domain.Follow;
import cocktail.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow,Long>,FollowRepositoryCustom {

    Follow findFollowByFromUserAndToUser(User user, User toUser);
    int findFollowerCountById(Long currentId);
    int findFollowingCountById(Long currentId);

//    List<Follow> findAllByFromUser(Long userId); // 사용자가 팔로우한 관계를 가져옴
//    List<Follow> findAllByToUser(Long userId);	 // 사용자를 팔로우하는 관계를 가져옴
//
//    // 팔로잉 목록 조회
//    Page<Follow> findAllByFromUser(Pageable pageable, User fromUser);
//
//    // 팔로워 목록 조회
//    Page<Follow> findAllByToUser(Pageable pageable, User toUser);

}
