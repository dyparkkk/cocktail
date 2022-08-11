package cocktail.application.recipe;

import cocktail.application.auth.SessionUser;
import cocktail.domain.recipe.Star;
import cocktail.domain.recipe.Recipe;
import cocktail.dto.StarDto;
import cocktail.infra.recipe.RecipeRepository;
import cocktail.infra.user.StarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StarService {

    private final StarRepository starRepository;
    private final RecipeRepository recipeRepository;

    @Transactional
    public void giveStar(StarDto dto, long recipeId, SessionUser sessionUser) {
//        starRepository.findByUsernameAndRecipeId(sessionUser.getUsername(),recipeId)
//                .ifPresent(star -> {throw new IllegalStateException("이미 평가했습니다");});

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> {throw new IllegalArgumentException("recipeId="+recipeId);});

        Star star = new Star(sessionUser.getUsername(), recipeId, dto.getRating());
        starRepository.save(star);

        recipe.updateStar(dto.getRating());
    }

}
