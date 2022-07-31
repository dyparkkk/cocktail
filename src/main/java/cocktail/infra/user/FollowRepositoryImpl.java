package cocktail.infra.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom{

//    private final JPAQueryFactory queryFactory;
//
////이렇게 하는게 맞는건지 모르겠어... 리스트 조회를 할려고 했는데;;
//    public Optional<Follow> findOptional(User user,User toUser){
//       Follow follow = queryFactory
//               .selectFrom(QFollow.follow)
//               .where(QFollow.follow.fromUser.eq(user),
//                       QFollow.follow.toUser.eq(toUser))
//               .fetchOne();
//       return Optional.ofNullable(follow);
//
//    }
//
//
//    public List<Follow> findAllByFromUser(Long userId ){
//       return queryFactory
//               .select(follow)
//               .from(follow.fromUser)
//               .join(follow).on(user.id.eq(userId))
//               .fetch();
//
//    }
}
