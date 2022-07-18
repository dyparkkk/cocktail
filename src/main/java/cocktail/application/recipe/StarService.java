package cocktail.application.recipe;

import cocktail.application.auth.SessionUser;
import cocktail.domain.Star;
import cocktail.domain.recipe.Recipe;
import cocktail.dto.StarDto;
import cocktail.infra.recipe.RecipeRepository;
import cocktail.infra.user.StarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StarService {

    private final StarRepository starRepository;
    private final RecipeRepository recipeRepository;

    public void giveStar(StarDto dto, SessionUser sessionUser) {
        starRepository.findByUsernameAndRecipeId(sessionUser.getUsername(), dto.getRecipeId())
                .ifPresent((star)-> {throw new IllegalStateException("이미 평가했습니다");});

        Star star = new Star(sessionUser.getUsername(), dto.getRecipeId(), dto.getRating());
        starRepository.save(star);

        Recipe recipe = recipeRepository.findById(dto.getRecipeId())
                .orElseThrow(() -> new IllegalStateException());

        recipe.updateStar(dto.getRating());
    }

}
