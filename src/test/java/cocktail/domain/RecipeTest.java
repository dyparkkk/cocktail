package cocktail.domain;

import cocktail.domain.recipe.Order;
import cocktail.domain.recipe.Recipe;
import cocktail.infra.recipe.ListToStringConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecipeTest {

    @Spy
    ListToStringConverter converter;

    @Test
    void orders의_순서_테스트() {
        // given
        ArrayList<Order> orders = new ArrayList<>(Arrays.asList(
                new Order(2, "흔든다"),
                new Order(1, "드라이 진과 올리브를 넣는다"),
                new Order(3, "마신다")));

        // when
        // 생성시 순서 보장이 됨
        Recipe recipe = Recipe.builder()
                .name("name")
                .dosu(BigDecimal.ZERO)
                .orders(orders)
                .build();

        // then
        List<Order> getOrders = recipe.getOrders();
        assertThat(getOrders).containsExactly(orders.get(1), orders.get(0), orders.get(2));
    }

    @Test
    @DisplayName("updateStar 메서드 동작시 star의 평균값을 다시 계산해서 저장한다. ")
    void updateStarTest() {
        Recipe recipe = Recipe.builder().build();

        // 4+2 / 2 = 3점
        recipe.updateStar(new BigDecimal("4"));
        recipe.updateStar(new BigDecimal("2"));

        assertThat(recipe.getStar().toString()).isEqualTo("3.00");
    }

    @Test
    @DisplayName("ListToStringConverter가 동작해서 List<String>을 String으로 변환해서 저장한다. ")
    void ListToStringConverter() {
        List<String> strings = Arrays.asList("https://test1.com", "https://test2.com");

        String result = converter.convertToDatabaseColumn(strings);

        assertThat(result).isEqualTo("https://test1.com;https://test2.com");
    }

    @Test
    @DisplayName("ListToStringConverter가 동작해서 String을 List<String>으로 다시 바꾼다. ")
    void StringToListConverter() {
         String str = "https://test1.com;https://test2.com";

        List<String> strings = converter.convertToEntityAttribute(str);

        assertThat(strings).containsExactly("https://test1.com", "https://test2.com");
    }
}