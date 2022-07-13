package cocktail.api;

import cocktail.application.auth.SessionUser;
import cocktail.application.recipe.FindRecipeService;
import cocktail.application.recipe.RecipeService;
import cocktail.dto.RecipeRequestDto;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
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
class RecipeControllerUnitTest {

    @Mock
    private RecipeService recipeService;
    @Mock
    private FindRecipeService findRecipeService;
    @InjectMocks
    private RecipeController recipeController;

    @Test
    @DisplayName("레시피 생성 api 유닛테스트 : 성공한다. ")
    void createRecipeSuccessTest(){
        RecipeRequestDto reqDto = RecipeRequestDto.builder()
                .name("name")
                .dosu(BigDecimal.ZERO).build();
        long recipeId = 1l;
        SessionUser sessionUser = new SessionUser(new UserDto("username", "nickname"));

        given(recipeService.createRecipe(reqDto, sessionUser)).willReturn(recipeId);

        ResponseEntity<Long> resEntity = recipeController.createRecipe(reqDto, sessionUser);
        assertThat(resEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resEntity.getBody()).isEqualTo(recipeId);
    }

    @Test
    @DisplayName("레시피 목록 보기 api 유닛테스트 : 성공한다. ")
    void viewRecipeSuccessTest() {
        List<RecipeResponseDto> recipeDtos =
                List.of(RecipeResponseDto.builder().id(1L).name("name1").build(),
                        RecipeResponseDto.builder().id(2L).name("name2").build());
        Pageable pageable = PageRequest.of(2, 5);

        given(findRecipeService.findAllPageable(any())).willReturn(recipeDtos);

        ResponseEntity<List<RecipeResponseDto>> resEntity = recipeController.viewAllRecipe(pageable);
        assertThat(resEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resEntity.getBody().size()).isEqualTo(2);
        assertThat(resEntity.getBody()).extracting("name").containsExactly("name1", "name2");
    }
}