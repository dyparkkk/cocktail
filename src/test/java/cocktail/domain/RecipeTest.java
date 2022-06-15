package cocktail.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    @Test
    void orders의_순서_테스트() {
        // given
        ArrayList<Order> orders = new ArrayList<>(Arrays.asList(
                new Order(2, "흔든다"),
                new Order(1, "드라이 진과 올리브를 넣는다"),
                new Order(3, "마신다")));

        // when
        // 생성시 순서 보장이 됨
        Recipe recipe = new Recipe("name", BigDecimal.ZERO, orders);

        // then
        List<Order> getOrders = recipe.getOrders();
        Assertions.assertThat(getOrders).containsExactly(orders.get(1), orders.get(0), orders.get(2));
    }
}