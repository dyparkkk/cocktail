package cocktail.api;


import cocktail.domain.Role;
import cocktail.domain.User;
import cocktail.domain.recipe.Base;
import cocktail.domain.recipe.Brewing;
import cocktail.domain.recipe.Ingredient;
import cocktail.domain.recipe.Order;
import cocktail.domain.recipe.Recipe;
import cocktail.domain.recipe.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @RequiredArgsConstructor
    static class InitRecipeService{

        private final PasswordEncoder passwordEncoder;

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            // user
            String pw = passwordEncoder.encode("pw");
            User user = new User("username@naver.com", pw, "nickname","title","profileImgUrl", Role.USER);
            em.persist(user);

            em.persist(new User("test@test.com", pw, "test", "title","profileImgUrl", Role.USER));

            // recipe
            List<Order> orderList = List.of(
                    new Order(1, "넣는다"),
                    new Order(2, "섞는다."),
                    new Order(3, "마신다."));

            for (int i = 0; i < 10; i++) {
                Base[] bases = Base.values();
                Brewing[] brewings = Brewing.values();
                String[] garnishes = {"콜라", "레몬", "설탕", "치킨무"};

                Recipe recipe = Recipe.builder()
                        .name("name" + i)
                        .dosu(new BigDecimal(i*10))
                        .base(bases[i % bases.length])
                        .brewing(brewings[i % brewings.length])
                        .soft(i)
                        .sweet(i)
                        .glass(String.valueOf(i)+"번 글래스")
                        .garnish(garnishes[i%4])
                        .imageUrls(List.of("https://cocktail-image.s3.ap-northeast-2.amazonaws.com/" + (i+1) + ".png"))
                        .orders(orderList).build();

                recipe.setUser(user);
                em.persist(recipe);

                em.persist(new Tag("tag"+String.valueOf(i%3), recipe));
                em.persist(new Tag("맛있는"+String.valueOf(i%2), recipe));

                em.persist(new Ingredient(1, "재료1", "45ml", recipe));
                em.persist(new Ingredient(2, "재료2", "1/2oz", recipe));
            }


        }
    }
}
