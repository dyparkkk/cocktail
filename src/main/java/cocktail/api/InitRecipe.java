package cocktail.api;


import cocktail.domain.recipe.Order;
import cocktail.domain.recipe.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitRecipe {

    private final InitRecipeService initRecipeService;

    @PostConstruct
    public void init() {
        initRecipeService.init();
    }

    @Component
    static class InitRecipeService{

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            List<Order> orderList = List.of(
                    new Order(1, "넣는다"),
                    new Order(2, "섞는다."),
                    new Order(3, "마신다."));

            for (int i = 0; i < 100; i++) {
                em.persist(
                        Recipe.builder()
                        .name("name"+i)
                        .dosu(new BigDecimal(i))
                        .orders(orderList).build());
            }
        }
    }
}
