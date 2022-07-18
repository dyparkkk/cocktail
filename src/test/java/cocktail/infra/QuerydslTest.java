package cocktail.infra;

import cocktail.domain.*;
import cocktail.domain.recipe.Recipe;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static cocktail.domain.recipe.QRecipe.recipe;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class QuerydslTest {

    @Autowired
    EntityManager em;

    @Test
    void JPQL_세팅확인() {
        User user = saveMember();

        User singleResult = (User) em.createQuery("select u from User u where u.username =: username")
                .setParameter("username", "name")
                .getSingleResult();

        assertThat(singleResult.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void querydsl_세팅확인() {
        User user = saveMember();

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QUser qUser = new QUser("u");

        List<User> result = queryFactory
                .selectFrom(qUser)
                .where(qUser.username.eq("name"))
                .fetch();

        assertThat(result).containsExactly(user);
    }

    @Test
    void querydslBetweenTest(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        em.persist(Recipe.builder().dosu(BigDecimal.ZERO).build());
        em.persist(Recipe.builder().dosu(BigDecimal.TEN).build());
        em.persist(Recipe.builder().dosu(new BigDecimal("15.15")).build());
        em.persist(Recipe.builder().dosu(new BigDecimal("30.0")).build());
        em.persist(Recipe.builder().dosu(new BigDecimal("40.0")).build());
        em.flush();
        em.clear();

        List<Recipe> result = queryFactory.selectFrom(recipe)
                .where(recipe.dosu.loe(new BigDecimal("30.0")),
                        recipe.dosu.goe(new BigDecimal("10.0")))
                .fetch();

        assertThat(result.size()).isEqualTo(3);
    }

    private User saveMember() {
        User user = User.builder()
                        .username("name")
                        .pw("pw")
                        .build();
        em.persist(user);
        return user;
    }
}
