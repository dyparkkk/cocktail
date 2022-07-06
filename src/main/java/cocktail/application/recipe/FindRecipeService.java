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

import static cocktail.dto.RecipeResponseDto.*;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class FindRecipeService {

    private final RecipeRepository recipeRepository;

    @Transactional
    public List<RecipeResponseDto> findAllPageable(Pageable pageable){
        return recipeRepository.findAllRecipe(pageable)
                .stream().map(RecipeResponseDto::fromEntity).collect(toList());
    }

    @Transactional
    public List<RecipeResponseDto> filterSearch(SearchCondition condition, Pageable pageable){
        return recipeRepository.filterSearch(condition, pageable)
                .stream().map(RecipeResponseDto::fromEntity).collect(toList());
    }

    @Transactional
    public DetailDto findById(Long id) {
        Recipe recipe = recipeRepository.fetchFindById(id)
                .orElseThrow(() -> new IllegalArgumentException("RecipeService.findById : id값을 찾을 수 없습니다."));

        recipeRepository.viewCntPlus(id);
        return DetailDto.from(recipe);
    }
}
