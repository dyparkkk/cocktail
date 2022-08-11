package cocktail.infra.user;

import cocktail.domain.user.Bookmark;
import cocktail.domain.user.User;
import cocktail.domain.recipe.Recipe;

import java.util.Optional;

public interface BookmarkRepositoryCustom {
    Optional<Bookmark> findOptional(User user, Recipe recipe);
}
