package cocktail.infra.user;

import cocktail.domain.Follow;
import cocktail.domain.QFollow;
import cocktail.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cocktail.domain.QUser.*;
import static cocktail.domain.QFollow.*;
@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom{

    private final JPAQueryFactory queryFactory;

//이렇게 하는게 맞는건지 모르겠어... 리스트 조회를 할려고 했는데;;
    public Optional<Follow> findOptional(User fromUser,User toUser){
       Follow follow = queryFactory
               .selectFrom(QFollow.follow)
               .where(QFollow.follow.fromUser.eq(fromUser),
                       QFollow.follow.toUser.eq(toUser))
               .fetchOne();
       return Optional.ofNullable(follow);

    }


    public List<Follow> findAllByFromUser(Long userId ){
       return queryFactory
               .select(follow)
               .from(follow.fromUser)
               .join(follow).on(user.id.eq(userId))
               .fetch();

    }
}
