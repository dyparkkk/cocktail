package cocktail.domain.recipe;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class TagTest {

    @Test
    @DisplayName("특정 태그(IBA)를 검사할 때 레시피의 오피셜 필드가 바뀐다")
    void officialTagTest() {

        Recipe recipe = Recipe.builder()
                .name("recipe")
                .build();

        List<Tag> tags = List.of(new Tag("tag1", recipe),
                                new Tag("IBA", recipe));

        Assertions.assertThat(recipe.getOfficial()).isEqualTo(Official.IBA);
    }

    @Test
    @DisplayName("특정 태그(KBMA)를 검사할 때 태그가 소문자여도 대문자로 바뀌서 검사하고 오피셜 필드가 KBMA로 바뀐다")
    void officialTagUpperCaseTest() {

        Recipe recipe = Recipe.builder()
                .name("recipe")
                .build();

        List<Tag> tags = List.of(new Tag("tag1", recipe),
                new Tag("kBma", recipe));

        Assertions.assertThat(recipe.getOfficial()).isEqualTo(Official.KBMA);
    }

    @Test
    @DisplayName("평범한 태그일 때는 recipe의 오피셜이 none으로 설정된다. ")
    void officialTagNoneTest() {

        Recipe recipe = Recipe.builder()
                .name("recipe")
                .build();

        List<Tag> tags = List.of(new Tag("tag1", recipe),
                new Tag("tag2", recipe));

        Assertions.assertThat(recipe.getOfficial()).isEqualTo(Official.NONE);
    }
}