package cocktail.application;

import cocktail.domain.Order;
import cocktail.domain.Recipe;
import cocktail.domain.Tag;
import cocktail.dto.RecipeRequestDto;
import cocktail.infra.RecipeRepository;
import cocktail.infra.TagRepository;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.stream.Collectors;

import static cocktail.dto.RecipeRequestDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @InjectMocks private RecipeService recipeService;
    @Mock RecipeRepository recipeRepository;
    @Mock TagRepository tagRepository;

    @Test
    void createRecipe_recipe생성() {
        // data
        RecipeRequestDto dto = createDto();
        Recipe recipe = createRecipe(dto);

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

        assertThat(findRecipe.getName()).isEqualTo(recipe.getName());
        assertThat(findRecipe.getDosu()).isEqualTo(recipe.getDosu());
        assertThat(findRecipe.getOrders()).contains(recipe.getOrders().get(0), recipe.getOrders().get(1));
    }

    private RecipeRequestDto createDto() {
        String name = "마티니";
        String dosu = "30.0";
        List<String> stringTagList = new ArrayList<>(Arrays.asList("드라이 진", "IBA", "젓지말고 흔들어서"));

        List<OrderDto> orderDtoList = new ArrayList<>(Arrays.asList(
                new OrderDto(1, "드라이 진과 올리브를 넣는다."),
                new OrderDto(2, "흔든다.")));

        return new RecipeRequestDto(name, dosu, stringTagList, orderDtoList);
    }

    private Recipe createRecipe(RecipeRequestDto dto) {
        List<Order> orderList = dto.getOrders().stream()
                .map(OrderDto::toOrder).collect(Collectors.toList());
        return new Recipe(dto.getName(), new BigDecimal(dto.getDosu()), orderList);
    }

    @Test
    void createRecipe_tag생성() {
        // data
        RecipeRequestDto dto = createDto();
        Recipe recipe = createRecipe(dto);

        Long id = 1l;
        ReflectionTestUtils.setField(recipe, "id", id);

        List<Tag> tagList = createTagList(dto, recipe);

        // BDDMockito
        given(tagRepository.saveAll(any()))
                .willReturn(tagList);
        given(tagRepository.findAll())
                .willReturn(tagList);

        // when
        recipeService.createRecipe(dto);

        // then
        List<Tag> findTagList = tagRepository.findAll();

        assertThat(findTagList.size()).isEqualTo(3);
        assertThat(findTagList).contains(tagList.get(0));
    }

    private List<Tag> createTagList(RecipeRequestDto dto, Recipe recipe) {
        return dto.getTags().stream().map(s -> new Tag(s, recipe)).collect(Collectors.toList());
    }

}