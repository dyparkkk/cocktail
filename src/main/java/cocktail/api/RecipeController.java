package cocktail.api;

import cocktail.application.RecipeService;
import cocktail.dto.RecipeRequestDto;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cocktail.dto.RecipeResponseDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<RecipeResponseDto> createRecipe(@RequestBody RecipeRequestDto dto) {
        Long recipeId = recipeService.createRecipe(dto);

        /**
         * ResponseEntity는 내부적으로 ObjectMapper를 사용함
         *  -> 매개변수 없는 생성자로 클래스 생성 후
         *    필드가 퍼블릭이거나 getter메서드가 필요 ( 그걸로 필드 값 주입)
         */
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RecipeResponseDto(recipeId));
    }

    @GetMapping
    public ResponseEntity<List<RecipeListDto>> viewAllRecipe(Pageable pageable) {
        List<RecipeListDto> list = recipeService.findAllPageable(pageable);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipeResponseDto>> searchRecipeApi(@RequestBody SearchCondition condition, Pageable pageable) {
        List<RecipeResponseDto> resList = recipeService.filterSearch(condition, pageable);
        return new ResponseEntity<>(resList, HttpStatus.OK);
    }
}
