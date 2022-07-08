package cocktail.application;

import cocktail.application.auth.SessionUser;
import cocktail.domain.Role;
import cocktail.domain.User;
import cocktail.domain.recipe.Base;
import cocktail.domain.recipe.Brewing;
import cocktail.domain.recipe.Order;
import cocktail.domain.recipe.Recipe;
import cocktail.dto.RecipeRequestDto;
import cocktail.dto.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Component
public class RecipeTestUtil {

    Recipe createRecipe() {
        Recipe recipe = Recipe.builder()
                .name("before")
                .dosu(BigDecimal.TEN)
                .brewing(Brewing.THROWING)
                .base(Base.RUM)
                .orders(List.of(new Order(1, "1111")))
                .glass("glass")
                .sweet(1)
                .soft(2)
                .garnishes(Set.of("레몬", "콜라"))
                .build();

        ReflectionTestUtils.setField(recipe, "createdDate", LocalDateTime.now());
        ReflectionTestUtils.setField(recipe, "lastModifiedDate", LocalDateTime.now());
        return recipe;
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
                .glass("glass")
                .garnish(List.of("셀러드 스틱", "레몬 웨지"))
                .soft(3)
                .sweet(10)
                .build();
    }

    Recipe dtoToRecipe(RecipeRequestDto dto) {
        List<Order> orderList = dto.getOrders().stream()
                .map(RecipeRequestDto.OrderDto::toOrder).collect(toList());
        Set<String> garnishes = Set.copyOf(dto.getGarnish());
        return Recipe.builder()
                .name(dto.getName())
                .dosu(dto.getDosu())
                .orders(orderList)
                .garnishes(garnishes)
                .soft(dto.getSoft())
                .sweet(dto.getSweet())
                .glass(dto.getGlass())
                .base(dto.getBase())
                .brewing(dto.getBrewing())
                .build();
    }

    User createUser() {
        return new User("username", "pw", "nickname", Role.USER);
    }
}
