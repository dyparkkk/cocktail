package cocktail.infra.recipe;

import cocktail.domain.recipe.Recipe;
import cocktail.domain.user.User;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.SearchCondition;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static cocktail.dto.RecipeResponseDto.*;

public interface RecipeRepositoryCustom {

    List<Recipe> findAllRecipe(Pageable pageable);
    List<Recipe> filterSearch(SearchCondition condition, Pageable pageable);
    long deleteTags(Long id);
    Optional<Recipe> fetchFindById(Long id);
    long deleteIngredients(Long id);
    long viewCntPlus(Long id);

    List<Recipe> findAllByUser(User user);
}
