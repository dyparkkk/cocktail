package cocktail.infra;

import cocktail.domain.recipe.*;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.SearchCondition;
import cocktail.infra.recipe.IngredientRepository;
import cocktail.infra.recipe.RecipeRepository;
import cocktail.infra.recipe.TagRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
class RecipeRepositoryImplTest {

    @Autowired
    EntityManager em;

    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    IngredientRepository ingredientRepository;

    private JPAQueryFactory queryFactory;

    @BeforeEach
    void before() {
        queryFactory = new JPAQueryFactory(em);

        Recipe recipe1 = Recipe.builder()
                .name("name1")
                .base(Base.GIN)
                .brewing(Brewing.BLENDING)
                .build();
        em.persist(recipe1);

        em.persist(new Tag("태그1", recipe1));
        em.persist(new Tag("맛있는1", recipe1));
        em.persist(new Ingredient(1, "베르무트", "45ml", recipe1));
        em.persist(new Ingredient(2, "레몬", "1pc", recipe1));

        Recipe recipe2 = Recipe.builder()
                .name("name2")
                .base(Base.BRANDY)
                .brewing(Brewing.FLOATING)
                .build();
        em.persist(recipe2);

        em.persist(new Tag("태그2", recipe2));
        em.persist(new Tag("맛있는2", recipe2));
        em.persist(new Ingredient(1, "베르무트2", "45ml", recipe2));
        em.persist(new Ingredient(2, "레몬2", "1pc", recipe2));
    }

    @Test
    @DisplayName("filter 검색이 태그에 맞게 걸러진다.")
    void filterSearchTagTest() {
        // given
        Pageable pageable = PageRequest.of(0, 3);
        SearchCondition condition = SearchCondition.builder()
                .name("")
                .tagList(List.of("태그1"))
                .build();

        //when
        List<Recipe> recipes = recipeRepository.filterSearch(condition, pageable);

        // then
        assertThat(recipes.size()).isEqualTo(1);
        assertThat(recipes.get(0).getName()).isEqualTo("name1");
    }

    @Test
    @DisplayName("filter 검색이 기주에 맞게 잘 걸러진다. ")
    void filterSearchBaseTest() {
        Pageable pageable = PageRequest.of(0, 3);
        SearchCondition condition = SearchCondition.builder()
                .name("")
                .base(Base.BRANDY)
                .build();

        //when
        List<Recipe> recipes = recipeRepository.filterSearch(condition, pageable);

        // then
        assertThat(recipes.size()).isEqualTo(1);
        assertThat(recipes.get(0).getName()).isEqualTo("name2");

    }

    @Test
    @DisplayName("filter 검색이 조주법에 맞게 잘 걸러진다.")
    void filterSearchBrewingTest() {
        Pageable pageable = PageRequest.of(0, 3);
        SearchCondition condition = SearchCondition.builder()
                .name("")
                .brewing(Brewing.BLENDING)
                .build();

        //when
        List<Recipe> recipes = recipeRepository.filterSearch(condition, pageable);

        // then
        assertThat(recipes.size()).isEqualTo(1);
        assertThat(recipes.get(0).getName()).isEqualTo("name1");
    }

    @Test
    @DisplayName("deleteTags 메서드가 recipe와 연관된 tags를 전부 삭제한다.")
    void deleteTagsTest() {
        Recipe recipe = recipeRepository.findAll().get(0);
        Long id = recipe.getId();

        // when
        recipeRepository.deleteTags(id);

        // then
        List<Tag> findTags = tagRepository.findAll();
        assertThat(findTags.size()).isEqualTo(2);
        assertThat(findTags).extracting("name").containsExactly("태그2", "맛있는2");
    }

    @Test
    @DisplayName("fetchFindById가 recipe 찾기에 성공한다.(query 한방으로) ")
    void fetchFindById_SuccessTest() {
        // given
        Recipe recipe = recipeRepository.findAll().get(0);
        Long id = recipe.getId();

        // when
        Recipe findRecipe = recipeRepository.fetchFindById(id).get();

        // then
        assertThat(findRecipe.getTags().size()).isEqualTo(2);
        assertThat(findRecipe.getTags()).extracting("name").containsExactly("태그1", "맛있는1");
        assertThat(findRecipe.getIngredients().size()).isEqualTo(2);
        assertThat(findRecipe.getIngredients()).extracting("volume").containsExactly("45ml", "1pc");
    }

    @Test
    @DisplayName("fetchFindById가 결과를 찾지 못하면 NoSuchElementException을 반환한다.")
    void fetchFindById_notFound_failTest() {
        // given
        Long id = 123L;

        // when, then
        assertThatThrownBy(() -> recipeRepository.fetchFindById(id).get())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("deleteIngredients 메서드가 recipe와 연관된 ingredients를 전부 삭제한다.")
    void deleteIngredientsTest() {
        Recipe recipe = recipeRepository.findAll().get(0);
        Long id = recipe.getId();

        // when
        recipeRepository.deleteIngredients(id);
        em.flush();
        em.clear();

        //then
        List<Ingredient> findIngredients = ingredientRepository.findAll();
        assertThat(findIngredients.size()).isEqualTo(2);
        assertThat(findIngredients).extracting("name").containsExactly("베르무트2", "레몬2");

        Recipe findRecipe1 = recipeRepository.findById(id).get();
        assertThat(findRecipe1.getIngredients().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("findAllRecipe가 최신순의 레시피를 반환한다. ")
    void findAllRecipe_created_orderBy() {
        Pageable pageable = PageRequest.of(0, 3);

        // when
        List<Recipe> recipes = recipeRepository.findAllRecipe(pageable);

        // then
        assertThat(recipes.get(0).getCreatedDate().isAfter(recipes.get(1).getCreatedDate())).isTrue();
    }

    @Test
    @DisplayName("findAllRecipe가 별점순의 레시피를 반환한다. ")
    void findAllRecipe_star_orderBy() {
        //given
        Pageable pageable = PageRequest.of(0, 4, Sort.by("star").descending());

        Recipe recipe = Recipe.builder()
                .name("name3").build();
        ReflectionTestUtils.setField(recipe, "star", "3.35");
        em.persist(recipe);

        Recipe recipe2 = Recipe.builder()
                .name("name4").build();
        ReflectionTestUtils.setField(recipe2, "star", "4.46");
        em.persist(recipe2);

        // when
        List<Recipe> recipes = recipeRepository.findAllRecipe(pageable);

        // then
        assertThat(recipes.get(0).getName()).isEqualTo("name4");
        assertThat(recipes.get(0).getStar()).isEqualTo("4.46");
    }
}