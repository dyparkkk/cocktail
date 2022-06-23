package cocktail.application;

import cocktail.domain.recipe.*;
import cocktail.dto.RecipeRequestDto;
import cocktail.dto.RecipeResponseDto;
import cocktail.infra.recipe.RecipeRepository;
import cocktail.infra.recipe.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static cocktail.dto.RecipeRequestDto.*;
import static cocktail.dto.RecipeResponseDto.*;
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

    @Test
    void updateTest() {
        // data
        Recipe recipe = createRecipe();
        Tag tag = new Tag("전태그", recipe);

        RecipeRequestDto dto = createDto();

        List<Tag> newTagList = dto.getTags().stream()
                .map(s -> new Tag(s, recipe)).collect(Collectors.toList());

        given(recipeRepository.findById(any()))
                .willReturn(Optional.ofNullable(recipe));
        given(recipeRepository.deleteTags(recipe.getId()))
                .willReturn(1L);
        given(tagRepository.saveAll(any()))
                .willReturn(newTagList);

        //when
        recipeService.update(recipe.getId(), dto);

        //then
        assertThat(recipe.getName()).isEqualTo("마티니");
        assertThat(recipe.getBrewing()).isEqualTo(Brewing.BLENDING);
        assertThat(recipe.getBase()).isEqualTo(Base.NONE);
        assertThat(recipe.getOrders()).extracting("content")
                .containsExactly("드라이 진과 올리브를 넣는다.", "흔든다.");
        assertThat(recipe.getTags()).extracting("name")
                .contains("드라이 진", "IBA", "젓지말고 흔들어서");
    }

    @Test
    void findById_Success_Test() {
        Recipe recipe = createRecipe();
        Long id = 1l;
        DetailDto dto = DetailDto.from(recipe);

        given(recipeRepository.fetchFindById(any()))
                .willReturn(Optional.ofNullable(recipe));

        //when
        DetailDto resultDto = recipeService.findById(id);

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
        assertThatThrownBy(()-> recipeService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
    private RecipeRequestDto createDto() {
        String name = "마티니";
        String dosu = "30.0";
        List<String> stringTagList = new ArrayList<>(Arrays.asList("드라이 진", "IBA", "젓지말고 흔들어서"));

        List<OrderDto> orderDtoList = new ArrayList<>(Arrays.asList(
                new OrderDto(1, "드라이 진과 올리브를 넣는다."),
                new OrderDto(2, "흔든다.")));

        return RecipeRequestDto.builder()
                .name(name)
                .dosu(new BigDecimal("30.0"))
                .brewing(Brewing.BLENDING)
                .base(Base.NONE)
                .orders(orderDtoList)
                .tags(stringTagList).build();
    }

    private Recipe createRecipe(RecipeRequestDto dto) {
        List<Order> orderList = dto.getOrders().stream()
                .map(OrderDto::toOrder).collect(Collectors.toList());
        return Recipe.builder()
                .name(dto.getName())
                .dosu(dto.getDosu())
                .orders(orderList)
                .build();
    }

    private List<Tag> createTagList(RecipeRequestDto dto, Recipe recipe) {
        return dto.getTags().stream().map(s -> new Tag(s, recipe)).collect(Collectors.toList());
    }

    private Recipe createRecipe() {
        return Recipe.builder()
                .name("before")
                .dosu(BigDecimal.TEN)
                .brewing(Brewing.THROWING)
                .base(Base.RUM)
                .orders(List.of(new Order(1, "1111")))
                .build();
    }

}