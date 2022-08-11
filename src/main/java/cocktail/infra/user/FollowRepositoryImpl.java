package cocktail.infra.user;

import cocktail.domain.user.Follow;
import cocktail.domain.user.QFollow;
import cocktail.domain.user.QUser;
import cocktail.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cocktail.domain.user.QFollow.*;
import static cocktail.domain.user.QUser.*;


@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public long unFollow(User fromUser , User toUser){
        return queryFactory
                .delete(follow)
                .where(follow.fromUser.eq(fromUser),
                        follow.toUser.eq(toUser))
                .execute();
    }

    @Override
    public List<Follow> findFollowingList(User fromUser) {
        return queryFactory
                .selectFrom(follow)
                .leftJoin(follow.toUser, user).fetchJoin()
                .where(follow.fromUser.eq(fromUser))
                .orderBy(follow.createdDate.desc())
                .fetch();
    }

    @Override
    public List<Follow> findFollowerList(User toUser) {
        return queryFactory
                .selectFrom(follow)
                .leftJoin(follow.fromUser, user).fetchJoin()
                .where(follow.toUser.eq(toUser))
                .orderBy(follow.createdDate.desc())
                .fetch();
    }

    @Override
    public Optional<Follow> findByUsers(User fromUser, User toUser) {
        return Optional.ofNullable(queryFactory
                .selectFrom(QFollow.follow)
                .where(QFollow.follow.toUser.eq(toUser),
                        QFollow.follow.fromUser.eq(fromUser))
                .fetchOne());
    }
}
