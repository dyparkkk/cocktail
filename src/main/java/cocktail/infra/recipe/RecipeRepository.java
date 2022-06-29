package cocktail.infra.recipe;

import cocktail.domain.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, RecipeRepositoryCustom {

}
