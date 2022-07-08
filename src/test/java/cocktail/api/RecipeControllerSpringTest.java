package cocktail.api;

import cocktail.domain.User;
import cocktail.domain.recipe.Recipe;
import cocktail.domain.recipe.Tag;
import cocktail.dto.RecipeResponseDto;
import cocktail.infra.recipe.RecipeRepository;
import cocktail.infra.recipe.TagRepository;
import cocktail.infra.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecipeControllerSpringTest {

    @LocalServerPort
    private int port;

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private RecipeRepository recipeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TagRepository tagRepository;

    @BeforeEach
    void before() throws InterruptedException {
        for(int i=0; i<10; i++){
            User user = User.builder().nickname("user" + i).build();
            userRepository.save(user);

            Recipe recipe = Recipe.builder()
                    .name("name" + i)
                    .build();
            recipe.setUser(user);
            ReflectionTestUtils.setField(recipe, "star", String.valueOf(i));
            recipeRepository.save(recipe);

            tagRepository.save(new Tag("태그" + i, recipe));
            Thread.sleep(100);
        }
    }


    @Test
    @DisplayName("page와 size가 잘 동작하고 최신순으로 레시피들을 반환한다. ")
    void viewAllRecipePageApi() {
        String url = "http://localhost:" +port+ "/v1/recipe";
        String param = "?page=0&size=3";

        //when
        ResponseEntity<RecipeResponseDto[]> response = restTemplate.getForEntity(url + param, RecipeResponseDto[].class);

        //then
        assertThat(response.getBody().length).isEqualTo(3);
        String date1 = response.getBody()[0].getCreatedDate();
        String date2 = response.getBody()[1].getCreatedDate();
        assertThat(date1.compareTo(date2)).isEqualTo(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("sort=star로 조회시 별점이 높은 순서로 레시피들을 반환한다. ")
    void viewAllRecipeSortApi() {
        String url = "http://localhost:" +port+ "/v1/recipe";
        String param = "?page=0&size=5&sort=star";

        //when
        ResponseEntity<RecipeResponseDto[]> response = restTemplate.getForEntity(url + param, RecipeResponseDto[].class);

        //then
        assertThat(response.getBody()[0].getStar()).isEqualTo("9");
        assertThat(response.getBody()[1].getStar()).isEqualTo("8");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}