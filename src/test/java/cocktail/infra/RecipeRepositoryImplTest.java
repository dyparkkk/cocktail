package cocktail.infra;

import cocktail.domain.recipe.Base;
import cocktail.domain.recipe.Brewing;
import cocktail.domain.recipe.Recipe;
import cocktail.domain.recipe.Tag;
import cocktail.dto.RecipeResponseDto;
import cocktail.dto.SearchCondition;
import cocktail.infra.recipe.RecipeRepository;
import cocktail.infra.recipe.TagRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    private JPAQueryFactory queryFactory;

    @BeforeEach
    void before(){
        queryFactory = new JPAQueryFactory(em);

        Recipe recipe1 = Recipe.builder()
                .name("name1")
                .base(Base.GIN)
                .brewing(Brewing.BLENDING)
                .build();
        em.persist(recipe1);

        em.persist(new Tag("태그1", recipe1));
        em.persist(new Tag("맛있는1", recipe1));

        Recipe recipe2 = Recipe.builder()
                .name("name2")
                .base(Base.BRANDY)
                .brewing(Brewing.FLOATING)
                .build();
        em.persist(recipe2);

        em.persist(new Tag("태그2", recipe2));
        em.persist(new Tag("맛있는2", recipe2));
    }

    @Test
    void filterSearchTagTest(){
        // given
        Pageable pageable = PageRequest.of(0, 3);
        SearchCondition condition = SearchCondition.builder()
                .name("")
                .tagList(List.of("태그1"))
                .build();

        //when
        List<RecipeResponseDto> result = recipeRepository.filterSearch(condition, pageable);

        // then
        assertThat(result.get(0).getName()).isEqualTo("name1");
    }

    @Test
    void filterSearchBaseTest() {
        Pageable pageable = PageRequest.of(0, 3);
        SearchCondition condition = SearchCondition.builder()
                .name("")
                .base(Base.BRANDY)
                .build();

        //when
        List<RecipeResponseDto> result = recipeRepository.filterSearch(condition, pageable);

        // then
        assertThat(result.get(0).getName()).isEqualTo("name2");

    }

    @Test
    void filterSearchBrewingTest() {
        Pageable pageable = PageRequest.of(0, 3);
        SearchCondition condition = SearchCondition.builder()
                .name("")
                .brewing(Brewing.BLENDING)
                .build();

        //when
        List<RecipeResponseDto> result = recipeRepository.filterSearch(condition, pageable);

        // then
        assertThat(result.get(0).getName()).isEqualTo("name1");
    }

    @Test
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
    void fetchFindById_SuccessTest(){
        // given
        Recipe recipe = recipeRepository.findAll().get(0);
        Long id = recipe.getId();

        // when
        Recipe findRecipe = recipeRepository.fetchFindById(id).get();

        // then
        assertThat(findRecipe.getTags().size()).isEqualTo(2);
        assertThat(findRecipe.getTags()).extracting("name").containsExactly("태그1", "맛있는1");
    }

    @Test
    void fetchFindById_notFound_failTest(){
        // given
        Long id = 123L;

        // when, then
        assertThatThrownBy(()->recipeRepository.fetchFindById(id).get())
                .isInstanceOf(NoSuchElementException.class);

    }
}