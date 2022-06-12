package cocktail.application;

import cocktail.dto.RecipeRequestDto;
import cocktail.infra.RecipeRepository;
import cocktail.infra.TagRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecipeServiceTest {

    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    TagRepository tagRepository;

    @AfterEach
    void after() {
        recipeRepository.deleteAll();
        tagRepository.deleteAll();
    }

    @Test
    void createRecipe_불러오기() {
        // data
        String name = "마티니";
        String dosu = "30.0";
        List<String> stringTagList = new ArrayList<>(Arrays.asList("드라이 진", "IBA", "젓지말고 흔들어서"));

        RecipeRequestDto dto = new RecipeRequestDto(name, dosu, stringTagList);

        // if


        // then
    }
}