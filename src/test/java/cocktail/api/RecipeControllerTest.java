package cocktail.api;

import cocktail.application.RecipeService;
import cocktail.domain.Recipe;
import cocktail.dto.RecipeRequestDto;
import cocktail.dto.RecipeResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static cocktail.dto.RecipeResponseDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    @Test
    void createRecipeSuccessTest(){
        Recipe recipe = Recipe.builder()
                .name("name")
                .dosu(BigDecimal.ZERO).build();
        RecipeRequestDto reqDto = new RecipeRequestDto("name", "0", null, null);
        long recipeId = 1l;

        given(recipeService.createRecipe(reqDto)).willReturn(recipeId);

        ResponseEntity<RecipeResponseDto> resEntity = recipeController.createRecipe(reqDto);
        assertThat(resEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resEntity.getBody().getId()).isEqualTo(recipeId);
    }

    @Test
    void viewRecipeSuccessTest() {
        List<RecipeListDto> recipeListDtos = List.of(new RecipeListDto("name1"), new RecipeListDto("name2"));
        Pageable pageable = PageRequest.of(2, 5);

        given(recipeService.findAllPageable(any())).willReturn(recipeListDtos);

        ResponseEntity<List<RecipeListDto>> resEntity = recipeController.viewAllRecipe(pageable);
        assertThat(resEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resEntity.getBody().size()).isEqualTo(2);
        assertThat(resEntity.getBody()).extracting("name").containsExactly("name1", "name2");
    }
}