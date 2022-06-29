package cocktail.application;

import cocktail.domain.recipe.Base;
import cocktail.domain.recipe.Brewing;
import cocktail.domain.recipe.Order;
import cocktail.domain.recipe.Recipe;
import cocktail.dto.RecipeRequestDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class RecipeTestUtil {

    Recipe createRecipe() {
        return Recipe.builder()
                .name("before")
                .dosu(BigDecimal.TEN)
                .brewing(Brewing.THROWING)
                .base(Base.RUM)
                .orders(List.of(new Order(1, "1111")))
                .build();
    }

    RecipeRequestDto createDto() {
        String name = "마티니";
        String dosu = "30.0";
        List<String> stringTagList = new ArrayList<>(Arrays.asList("드라이 진", "IBA", "젓지말고 흔들어서"));

        List<RecipeRequestDto.OrderDto> orderDtoList = new ArrayList<>(Arrays.asList(
                new RecipeRequestDto.OrderDto(1, "드라이 진과 올리브를 넣는다."),
                new RecipeRequestDto.OrderDto(2, "흔든다.")));

        List<RecipeRequestDto.IngredientDto> ingredientDtoList = new ArrayList<>(Arrays.asList(
                new RecipeRequestDto.IngredientDto(1, "드라이 진", "60ml"),
                new RecipeRequestDto.IngredientDto(2, "베르무트", "10ml")
        ));

        return RecipeRequestDto.builder()
                .name(name)
                .dosu(new BigDecimal("30.0"))
                .brewing(Brewing.BLENDING)
                .base(Base.NONE)
                .orders(orderDtoList)
                .tags(stringTagList)
                .ingredients(ingredientDtoList)
                .build();
    }

    Recipe dtoToRecipe(RecipeRequestDto dto) {
        List<Order> orderList = dto.getOrders().stream()
                .map(RecipeRequestDto.OrderDto::toOrder).collect(toList());
        return Recipe.builder()
                .name(dto.getName())
                .dosu(dto.getDosu())
                .orders(orderList)
                .build();
    }
}
