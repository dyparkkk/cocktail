package cocktail.api;

import cocktail.application.recipe.FindRecipeService;
import cocktail.application.recipe.RecipeService;
import cocktail.domain.User;
import cocktail.domain.recipe.Recipe;
import cocktail.domain.recipe.Tag;
import cocktail.dto.RecipeResponseDto;
import cocktail.infra.recipe.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class RecipeControllerSpringTest {

    @LocalServerPort
    private int port;

    @Autowired TestRestTemplate restTemplate;
    @Autowired EntityManager em;
    @Autowired
    FindRecipeService findRecipeService;

    @BeforeEach
    void before(){
        for(int i=0; i<5; i++){
            User user = User.builder().nickname("user" + i).build();
            em.persist(user);

            Recipe recipe = Recipe.builder()
                    .name("testName" + i)
                    .build();
            recipe.setUser(user);
            ReflectionTestUtils.setField(recipe, "star", BigDecimal.valueOf(i));
            em.persist(recipe);

            em.persist(new Tag("??????" + i, recipe));
        }
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("page??? size??? ??? ???????????? ??????????????? ??????????????? ????????????. ")
    void viewAllRecipePageApi(){
        String url = "http://localhost:" +port+ "/v1/recipe";
        String param = "?page=0&size=3";

        //when
        ResponseEntity<RecipeResponseDto[]> response = restTemplate.getForEntity(url + param, RecipeResponseDto[].class);

        //then
//        assertThat(response.getBody().length).isEqualTo(3);
//        String date1 = response.getBody()[0].getCreatedDate();
//        System.out.println(date1);
//        String date2 = response.getBody()[1].getCreatedDate();
//        System.out.println(date2);
//        assertThat(date1.compareTo(date2)).isPositive();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("sort=star??? ????????? ????????? ?????? ????????? ??????????????? ????????????. ")
    void viewAllRecipeSortApi(){
        String url = "http://localhost:" +port+ "/v1/recipe";
        String param = "?page=0&size=5&sort=star";

        //when
        ResponseEntity<RecipeResponseDto[]> response = restTemplate.getForEntity(url + param, RecipeResponseDto[].class);

        //then
//        assertThat(response.getBody()[0].getStar().toString()).isEqualTo("9.00");
//        assertThat(response.getBody()[1].getStar().toString()).isEqualTo("8.00");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}