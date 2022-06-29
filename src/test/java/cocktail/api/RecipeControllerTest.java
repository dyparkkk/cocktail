package cocktail.api;

import cocktail.application.recipe.FindRecipeService;
import cocktail.application.recipe.MakeRecipeService;
import cocktail.dto.RecipeRequestDto;
import cocktail.dto.RecipeResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @Mock
    private MakeRecipeService makeRecipeService;
    @Mock
    private FindRecipeService findRecipeService;
    @InjectMocks
    private RecipeController recipeController;

    @Test
    void createRecipeSuccessTest(){
        RecipeRequestDto reqDto = RecipeRequestDto.builder()
                .name("name")
                .dosu(BigDecimal.ZERO).build();
        long recipeId = 1l;

        given(makeRecipeService.createRecipe(reqDto)).willReturn(recipeId);

        ResponseEntity<Long> resEntity = recipeController.createRecipe(reqDto);
        assertThat(resEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resEntity.getBody()).isEqualTo(recipeId);
    }

    @Test
    void viewRecipeSuccessTest() {
        List<RecipeResponseDto> recipeDtos =
                List.of(new RecipeResponseDto(1L, "name1"),
                new RecipeResponseDto(2L,"name2"));
        Pageable pageable = PageRequest.of(2, 5);

        given(findRecipeService.findAllPageable(any())).willReturn(recipeDtos);

        ResponseEntity<List<RecipeResponseDto>> resEntity = recipeController.viewAllRecipe(pageable);
        assertThat(resEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resEntity.getBody().size()).isEqualTo(2);
        assertThat(resEntity.getBody()).extracting("name").containsExactly("name1", "name2");
    }
}