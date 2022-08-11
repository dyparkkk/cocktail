package cocktail.infra.user;

import cocktail.domain.recipe.Star;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StarRepository extends JpaRepository<Star, Long> {

    Optional<Star> findByUsernameAndRecipeId(String username, long recipeId);
}
