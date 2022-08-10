package cocktail.api;

import cocktail.application.auth.SessionUser;
import cocktail.application.recipe.FindRecipeService;
import cocktail.application.recipe.RecipeService;
import cocktail.dto.RecipeRequestDto;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.SearchCondition;
import cocktail.global.config.Login;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
    private final FindRecipeService findRecipeService;

    @PostMapping
    @ApiOperation(value = "create a recipe")
    public ResponseEntity<Long> createRecipe(@RequestBody RecipeRequestDto dto,
                                             @ApiIgnore @Login SessionUser sessionUser) {
        Long recipeId = recipeService.createRecipe(dto, sessionUser);

        /**
         * ResponseEntity는 내부적으로 ObjectMapper를 사용함
         *  -> 매개변수 없는 생성자로 클래스 생성 후
         *    필드가 퍼블릭이거나 getter메서드가 필요 ( 그걸로 필드 값 주입)
         */
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recipeId);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "find detail recipe")
    @ApiImplicitParam(name = "id", dataType = "int",example = "3",value = "recipe_ID")
    public ResponseEntity<DetailDto> findByIdApi(@PathVariable Long id,
                                                 @Login SessionUser sessionUser) {
        DetailDto res = findRecipeService.findById(id);
        if(sessionUser != null){
            res.setIsWriter(findRecipeService.isWriter(id, sessionUser));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(res);
    }

    @GetMapping
    @ApiOperation(value = "find recipe list (find all recipe)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int",example = "0", value = "몇 페이지 (0부터 시장)"),
            @ApiImplicitParam(name = "size", dataType = "int",example = "3", value = "페이지의 요소 수(default 10)"),
            @ApiImplicitParam(name = "sort", dataType = "string",example = "star", value = "별점 순으로 정열")
    })
    public ResponseEntity<List<RecipeResponseDto>> viewAllRecipe(@ApiIgnore @PageableDefault Pageable pageable) {

        List<RecipeResponseDto> list = findRecipeService.findAllPageable(pageable);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/search")
    @ApiOperation(value = "search recipe list with filter")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", example = "0", value = "몇 페이지 (0부터 시장)"),
            @ApiImplicitParam(name = "size", dataType = "int", example = "3", value = "페이지의 데이터 수(default 10)"),
            @ApiImplicitParam(name = "sort", dataType = "string",example = "star", value = "별점 순으로 정열")
    })
    public ResponseEntity<List<RecipeResponseDto>> searchRecipeApi(@RequestBody SearchCondition condition,
                                                                   @ApiIgnore @PageableDefault Pageable pageable) {

        List<RecipeResponseDto> resList = findRecipeService.filterSearch(condition, pageable);
        return new ResponseEntity<>(resList, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "update recipe ")
    @ApiImplicitParam(name = "id", dataType = "int",example = "3", value = "recipe_ID")
    public ResponseEntity<Long> updateApi(@PathVariable Long id,
                                          @RequestBody RecipeRequestDto dto,
                                          @ApiIgnore @Login SessionUser sessionUser) {
        Long recipeId = recipeService.update(id, dto, sessionUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(recipeId);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "delete recipe ")
    @ApiImplicitParam(name = "id", dataType = "int",example = "3", value = "recipe_ID")
    public ResponseEntity<String> deleteApi(@PathVariable Long id,
                                            @ApiIgnore @Login SessionUser sessionUser) {
        recipeService.deleteRecipe(id, sessionUser);

        return new ResponseEntity<>("delete", HttpStatus.ACCEPTED);
    }


}
