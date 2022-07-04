package cocktail.api;


import cocktail.domain.Role;
import cocktail.domain.User;
import cocktail.domain.recipe.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
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
    @RequiredArgsConstructor
    static class InitRecipeService{

        private final PasswordEncoder passwordEncoder;
        private final HttpSession httpSession;

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            // user
            String pw = passwordEncoder.encode("pw");
            User user = new User("username@naver.com", pw, "nikname", Role.USER);
            em.persist(user);

            // recipe
            List<Order> orderList = List.of(
                    new Order(1, "넣는다"),
                    new Order(2, "섞는다."),
                    new Order(3, "마신다."));

            for (int i = 0; i < 10; i++) {
                Base[] bases = Base.values();
                Brewing[] brewings = Brewing.values();

                Recipe recipe = Recipe.builder()
                        .name("name" + i)
                        .dosu(new BigDecimal(i))
                        .base(bases[i % bases.length])
                        .brewing(brewings[i % brewings.length])
                        .orders(orderList).build();

                recipe.setUser(user);
                em.persist(recipe);

                em.persist(new Tag("tag"+String.valueOf(i%3), recipe));
                em.persist(new Tag("맛있는"+String.valueOf(i%2), recipe));

                em.persist(new Ingredient(1, "재료1", "45ml", recipe));
                em.persist(new Ingredient(1, "재료2", "1/2oz", recipe));
            }


        }
    }
}
