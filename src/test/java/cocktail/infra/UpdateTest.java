package cocktail.infra;

import cocktail.domain.recipe.Base;
import cocktail.domain.recipe.Recipe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class UpdateTest {

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("jpa의 더티체크 update 확인 (엔티티 참조를 바꾸면 더티체크가 작동하지 않는다)")
    void updateTest() {
        Recipe recipe = Recipe.builder().name("hi").build();
        em.persist(recipe);
        Long id = recipe.getId();
        em.flush();
        em.clear();

        Recipe findRecipe = em.find(Recipe.class, id);
        findRecipe = Recipe.builder().name("after").base(Base.BRANDY).build();
        em.flush();
        em.clear();

        Recipe secondRecipe = em.find(Recipe.class, id);
        assertThat(secondRecipe.getId()).isEqualTo(id);
        assertThat(secondRecipe.getName()).isEqualTo("hi");
    }
}
