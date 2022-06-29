package cocktail.application;

import cocktail.application.recipe.FindRecipeService;
import cocktail.domain.recipe.Recipe;
import cocktail.dto.RecipeResponseDto;
import cocktail.infra.recipe.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FindRecipeServiceTest {

    @InjectMocks
    private FindRecipeService findRecipeService;
    @Mock
    RecipeRepository recipeRepository;

    @Spy
    private RecipeTestUtil recipeTestUtil;

    @Test
    void findById_Success_Test() {
        Recipe recipe = recipeTestUtil.createRecipe();
        Long id = 1l;
        RecipeResponseDto.DetailDto dto = RecipeResponseDto.DetailDto.from(recipe);

        given(recipeRepository.fetchFindById(any()))
                .willReturn(Optional.ofNullable(recipe));

        //when
        RecipeResponseDto.DetailDto resultDto = findRecipeService.findById(id);

        //then
        assertThat(resultDto.getName()).isEqualTo(recipe.getName());
        assertThat(resultDto.getBrewing()).isEqualTo(recipe.getBrewing());
        assertThat(resultDto.getOrders()).extracting("content").containsExactly("1111");
    }

    @Test
    void findById_ID를_찾지_못함_Test(){
        // given
        Long id = 123L;

        given(recipeRepository.fetchFindById(any()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(()-> findRecipeService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
