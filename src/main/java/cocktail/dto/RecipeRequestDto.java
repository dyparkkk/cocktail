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
    private List<Order> orders;

    @Builder
    public RecipeRequestDto(String name, String dosu, List<String> tags, List<Order> orders) {
        this.name = name;
        this.dosu = dosu;
        this.tags = tags;
        this.orders = orders;
    }
}
