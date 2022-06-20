package cocktail.infra;

import cocktail.domain.Recipe;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.SearchCondition;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static cocktail.dto.RecipeResponseDto.*;

public interface RecipeRepositoryCustom {

    List<RecipeListDto> findAllListDto(Pageable pageable);
    List<RecipeResponseDto> filterSearch(SearchCondition condition, Pageable pageable);
    long deleteTags(Long id);
}
