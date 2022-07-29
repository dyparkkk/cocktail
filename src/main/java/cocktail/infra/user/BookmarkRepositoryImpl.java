package cocktail.infra.user;

import cocktail.domain.Bookmark;
import cocktail.domain.QBookmark;
import cocktail.domain.User;
import cocktail.domain.recipe.Recipe;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Bookmark> findOptional(User user, Recipe recipe) {
        Bookmark bookmark = queryFactory
                .selectFrom(QBookmark.bookmark)
                .where(QBookmark.bookmark.user.eq(user),
                        QBookmark.bookmark.recipe.eq(recipe))
                .fetchOne();
        return Optional.ofNullable(bookmark);
    }
}
