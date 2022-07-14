package cocktail.application;

import cocktail.application.auth.SessionUser;
import cocktail.application.recipe.RecipeService;
import cocktail.domain.User;
import cocktail.domain.recipe.*;
import cocktail.dto.RecipeRequestDto;
import cocktail.infra.recipe.IngredientRepository;
import cocktail.infra.recipe.RecipeRepository;
import cocktail.infra.recipe.TagRepository;
import cocktail.infra.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @InjectMocks private RecipeService recipeService;
    @Mock RecipeRepository recipeRepository;
    @Mock TagRepository tagRepository;
    @Mock IngredientRepository ingredientRepository;
    @Mock UserRepository userRepository;

    @Spy
    RecipeTestUtil recipeTestUtil;

    @Test
    @DisplayName("레시피 생성 시 유저를 참조한다. ")
    void createRecipe_recipe생성() {
        // data
        RecipeRequestDto dto = recipeTestUtil.createDto();
        Recipe recipe = recipeTestUtil.dtoToRecipe(dto);
        User user = recipeTestUtil.createUser();

        Long id = 1l;
        ReflectionTestUtils.setField(recipe, "id", id);

        // BDDMockito
        given(recipeRepository.findById(id))
                .willReturn(Optional.of(recipe));
        given(userRepository.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));

        // when
        recipeService.createRecipe(dto, new SessionUser(user));

        // then
        Recipe findRecipe = recipeRepository.findById(id).get();

        assertThat(findRecipe.getName()).isEqualTo(recipe.getName());
        assertThat(findRecipe.getDosu()).isEqualTo(recipe.getDosu());
        assertThat(findRecipe.getOrders()).contains(recipe.getOrders().get(0), recipe.getOrders().get(1));
    }

    @Test
    @DisplayName("레시피 생성 시 태그들이 생성되고 레시피의 참조가 그것들을 가리킨다. ")
    void createRecipe_tag생성() {
        // data
        RecipeRequestDto dto = recipeTestUtil.createDto();
        Recipe recipe = recipeTestUtil.dtoToRecipe(dto);
        User user = recipeTestUtil.createUser();

        List<Tag> tagList = createTagList(dto, recipe);

        // BDDMockito
        given(tagRepository.findAll())
                .willReturn(tagList);
        given(userRepository.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));

        // when
        recipeService.createRecipe(dto, new SessionUser(user));

        // then
        List<Tag> findTagList = tagRepository.findAll();
        assertThat(findTagList.size()).isEqualTo(3);
        assertThat(findTagList).contains(tagList.get(0));
    }

    @Test
    @DisplayName("레시피 생성 시 재료들이 생성되고 레시피의 참조가 그것들을 가리킨다. ")
    void createRecipe_ingredient생성_test(){
        RecipeRequestDto dto = recipeTestUtil.createDto();
        Recipe recipe = recipeTestUtil.dtoToRecipe(dto);
        User user = recipeTestUtil.createUser();

        Long id = 1l;
        ReflectionTestUtils.setField(recipe, "id", id);

        List<Ingredient> ingredients = dtoToIngredients(dto, recipe);

        given(ingredientRepository.findAll())
                .willReturn(ingredients);
        given(userRepository.findByUsername(user.getUsername()))
                .willReturn(Optional.of(user));

        //when
        recipeService.createRecipe(dto, new SessionUser(user));

        // then
        List<Ingredient> findIngredients = ingredientRepository.findAll();
        assertThat(findIngredients.size()).isEqualTo(2);
        assertThat(findIngredients).extracting("name").containsExactly("드라이 진", "베르무트");
        assertThat(findIngredients).extracting("volume").containsExactly("60ml", "10ml");

    }

    private List<Ingredient> dtoToIngredients(RecipeRequestDto dto, Recipe recipe) {
        return dto.getIngredients().stream()
                .map(ingredientDto -> ingredientDto.toEntity(recipe))
                .collect(toList());
    }

    @Test
    @DisplayName("update메서드가 dto의 값들로 바뀐다.")
    void updateTest() {
        // data
        Recipe recipe = recipeTestUtil.createRecipe();
        Long id = 1l;
        ReflectionTestUtils.setField(recipe, "id", id);
        Tag tag = new Tag("전태그", recipe);

        RecipeRequestDto dto = recipeTestUtil.createDto();
        User user = recipeTestUtil.createUser();

        List<Tag> newTagList = dto.getTags().stream()
                .map(s -> new Tag(s, recipe)).collect(toList());
        recipe.setUser(user);

        given(recipeRepository.fetchFindById(any()))
                .willReturn(Optional.of(recipe));
        //when
        recipeService.update(1L, dto, new SessionUser(user));

        //then
        assertThat(recipe.getName()).isEqualTo("마티니");
        assertThat(recipe.getBrewing()).isEqualTo(Brewing.BLENDING);
        assertThat(recipe.getBase()).isEqualTo(Base.NONE);
        assertThat(recipe.getOrders()).extracting("content")
                .containsExactly("드라이 진과 올리브를 넣는다.", "흔든다.");
        assertThat(recipe.getTags()).extracting("name")
                .contains("드라이 진", "IBA", "젓지말고 흔들어서");
        assertThat(recipe.getIngredients()).extracting("name")
                .contains("드라이 진", "베르무트");
        assertThat(recipe.getGarnish()).isEqualTo("셀러드 스틱, 레몬 웨지");
        assertThat(recipe.getGlass()).isEqualTo("glass");
    }

    private List<Tag> createTagList(RecipeRequestDto dto, Recipe recipe) {
        return dto.getTags().stream().map(s -> new Tag(s, recipe)).collect(toList());
    }



}