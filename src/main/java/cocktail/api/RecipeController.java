package cocktail.api;

import cocktail.application.RecipeService;
import cocktail.dto.RecipeRequestDto;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.SearchCondition;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

import static cocktail.dto.RecipeResponseDto.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/recipe")
@Api(tags = "recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<Long> createRecipe(@RequestBody RecipeRequestDto dto) {
        Long recipeId = recipeService.createRecipe(dto);

        /**
         * ResponseEntity는 내부적으로 ObjectMapper를 사용함
         *  -> 매개변수 없는 생성자로 클래스 생성 후
         *    필드가 퍼블릭이거나 getter메서드가 필요 ( 그걸로 필드 값 주입)
         */
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recipeId);
    }

    @GetMapping("/{id}")
    @ApiImplicitParam(name = "id", dataType = "int", value = "recipe_ID")
    public ResponseEntity<DetailDto> findByIdApi(@PathVariable Long id) {
        DetailDto res = recipeService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(res);
    }

    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", value = "몇 페이지 (0부터 시장)"),
            @ApiImplicitParam(name = "size", dataType = "int", value = "페이지의 요소 수(default 10)")
    })
    public ResponseEntity<List<RecipeResponseDto>> viewAllRecipe(@ApiIgnore @PageableDefault Pageable pageable) {

        List<RecipeResponseDto> list = recipeService.findAllPageable(pageable);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/search")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",dataType ="int", value="몇 페이지 (0부터 시장)"),
            @ApiImplicitParam(name="size",dataType ="int", value="페이지의 데이터 수(default 10)")
    })
    public ResponseEntity<List<RecipeResponseDto>> searchRecipeApi(@RequestBody SearchCondition condition,
                                                                   @ApiIgnore @PageableDefault Pageable pageable) {
        List<RecipeResponseDto> resList = recipeService.filterSearch(condition, pageable);
        return new ResponseEntity<>(resList, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiImplicitParam(name = "id", dataType = "int", value = "recipe_ID")
    public ResponseEntity<Long> updateApi(@PathVariable Long id,
                          @RequestBody RecipeRequestDto dto) {
        Long recipeId = recipeService.update(id, dto);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(recipeId);
    }
}
