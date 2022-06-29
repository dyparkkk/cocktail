package cocktail.application.recipe;

import cocktail.domain.recipe.Recipe;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.SearchCondition;
import cocktail.infra.recipe.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindRecipeService {

    private final RecipeRepository recipeRepository;

    @Transactional
    public List<RecipeResponseDto> findAllPageable(Pageable pageable){
        return recipeRepository.findAllListDto(pageable);
    }

    @Transactional
    public List<RecipeResponseDto> filterSearch(SearchCondition condition, Pageable pageable){
        return recipeRepository.filterSearch(condition, pageable);
    }

    @Transactional
    public RecipeResponseDto.DetailDto findById(Long id) {
        Recipe recipe = recipeRepository.fetchFindById(id)
                .orElseThrow(() -> new IllegalArgumentException("RecipeService.findById : id값을 찾을 수 없습니다."));

        return RecipeResponseDto.DetailDto.from(recipe);
    }
}
