package cocktail.application;

import cocktail.domain.Order;
import cocktail.domain.Recipe;
import cocktail.dto.RecipeRequestDto;
import cocktail.infra.RecipeRepository;
import cocktail.infra.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @InjectMocks private RecipeService recipeService;
    @Mock RecipeRepository recipeRepository;
    @Mock TagRepository tagRepository;

    @Test
    void createRecipe_recipe불러오기() {
        // data
        RecipeRequestDto dto = getRequestDto();
        Recipe recipe = getRecipe(dto);

        Long id = 1l;
        ReflectionTestUtils.setField(recipe, "id", id);

        // BDDMockito
        given(recipeRepository.save(any()))
                .willReturn(recipe);
        given(recipeRepository.findById(id))
                .willReturn(Optional.ofNullable(recipe));

        // when
        recipeService.createRecipe(dto);

        // then
        Recipe findRecipe = recipeRepository.findById(id).get();

        String findName = (String) ReflectionTestUtils.getField(findRecipe, "name");
        assertThat(dto.getName()).isEqualTo(findName);

        BigDecimal findDosu = (BigDecimal) ReflectionTestUtils.getField(findRecipe, "dosu");
        assertThat(dto.getDosu()).isEqualTo(findDosu.toString());

        List<Order> findOrders = findRecipe.getOrders();
        assertThat(findOrders).contains(dto.getOrders().get(0), dto.getOrders().get(1));
    }

    private Recipe getRecipe(RecipeRequestDto dto) {
        BigDecimal dosu = BigDecimal.valueOf(Double.valueOf(dto.getDosu()));
        return new Recipe(dto.getName(), dosu, dto.getOrders());
    }

    private RecipeRequestDto getRequestDto() {
        String name = "마티니";
        String dosu = "30.0";
        List<String> stringTagList = new ArrayList<>(Arrays.asList("드라이 진", "IBA", "젓지말고 흔들어서"));

        Order order1 = new Order(1, "드라이 진과 올리브를 넣는다.");
        Order order2 = new Order(2, "흔든다.");
        List<Order> orderList = new ArrayList<>(Arrays.asList(order1, order2));

        return new RecipeRequestDto(name, dosu, stringTagList, orderList);
    }

    @Test
    void createRecipe_recipe_Orders_확인() {

    }
}