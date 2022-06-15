package cocktail.dto;

import cocktail.domain.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RecipeRequestDto {
    private String name;
    // 검증 xx.x 형식
    private String dosu;
    private List<String> tags;
    private List<OrderDto> orders;

    @Builder
    public RecipeRequestDto(String name, String dosu, List<String> tags, List<OrderDto> orders) {
        this.name = name;
        this.dosu = dosu;
        this.tags = tags;
        this.orders = orders;
    }

    @NoArgsConstructor
    public static class OrderDto{
        private int num;
        private String content;

        public OrderDto(int num, String content) {
            this.num = num;
            this.content = content;
        }

        public Order toOrder(){
            return new Order(num, content);
        }
    }
}
