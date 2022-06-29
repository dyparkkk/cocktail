package cocktail.infra.user;

import cocktail.domain.Bookmark;
import cocktail.domain.User;
import cocktail.domain.recipe.Recipe;

import java.util.Optional;

public interface BookmarkRepositoryCustom {
    Optional<Bookmark> findOptional(User user, Recipe recipe);
}
