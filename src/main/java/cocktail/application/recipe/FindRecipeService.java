package cocktail.application.recipe;

import cocktail.application.auth.SessionUser;
import cocktail.domain.recipe.Recipe;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.SearchCondition;
import cocktail.infra.recipe.RecipeRepository;
import cocktail.infra.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
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
    private final UserRepository userRepository;

    @Transactional
    public List<RecipeResponseDto> findAllPageable(Pageable pageable){
        List<Recipe> recipes = recipeRepository.findAllRecipe(pageable);
        return recipes.stream()
                .map(RecipeResponseDto::fromEntity)
                .collect(toList());
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
//        Hibernate.initialize(recipe);
        return DetailDto.from(recipe);
    }

    @Transactional
    public boolean isWriter(Long recipeId, SessionUser user) {
        if(user == null) return false;
        Recipe recipe = recipeRepository.fetchFindById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("RecipeService.findById : id값을 찾을 수 없습니다."));

        if(user.getUsername().equals(recipe.getUser().getUsername())){
            return true;
        }
        return false;
    }
}
