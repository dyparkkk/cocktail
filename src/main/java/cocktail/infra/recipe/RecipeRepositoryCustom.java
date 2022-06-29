package cocktail.infra.recipe;

import cocktail.domain.recipe.Recipe;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.SearchCondition;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static cocktail.dto.RecipeResponseDto.*;

public interface RecipeRepositoryCustom {

    List<RecipeResponseDto> findAllListDto(Pageable pageable);
    List<RecipeResponseDto> filterSearch(SearchCondition condition, Pageable pageable);
    long deleteTags(Long id);
    Optional<Recipe> fetchFindById(Long id);
    long deleteIngredients(Long id);
}
