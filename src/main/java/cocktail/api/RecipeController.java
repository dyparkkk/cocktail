package cocktail.api;

import cocktail.application.RecipeService;
import cocktail.dto.RecipeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity createRecipe(@RequestBody RecipeRequestDto dto) {
        Long recipeId = recipeService.createRecipe(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recipeId);
    }
}
