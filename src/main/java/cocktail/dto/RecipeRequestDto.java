package cocktail.dto;

import cocktail.domain.recipe.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class RecipeRequestDto {
    private String name;
    // 검증 xx.x 형식
    private BigDecimal dosu;
    private Brewing brewing;
    private Base base;
    private List<String> garnish;
    private String glass;
    private Integer soft; // 0-9
    private Integer sweet; // 0-9
    private List<String> tags;
    private List<OrderDto> orders;
    private List<IngredientDto> ingredients;
    private LocalDateTime created;

    @Builder
    public RecipeRequestDto(String name, BigDecimal dosu, Brewing brewing, Base base, List<String> garnish, String glass, Integer soft, Integer sweet, List<String> tags, List<OrderDto> orders, List<IngredientDto> ingredients, LocalDateTime created) {
        this.name = name;
        this.dosu = dosu;
        this.brewing = brewing;
        this.base = base;
        this.garnish = garnish;
        this.glass = glass;
        this.soft = soft;
        this.sweet = sweet;
        this.tags = tags;
        this.orders = orders;
        this.ingredients = ingredients;
        this.created = created;
    }

    public void setBrewing(String brewing) {
        this.brewing = Brewing.valueOf(brewing.toUpperCase());
    }

    public void setBase(String base){
        this.base = Base.valueOf(base.toUpperCase());
    }

    public void setDosu(String dosu) {
        this.dosu = new BigDecimal(dosu.toUpperCase());
    }

    @Getter
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

    @Getter
    @NoArgsConstructor
    public static class IngredientDto {
        private int num;
        private String name;
        private String volume;

        public IngredientDto(int num, String name, String volume) {
            this.num = num;
            this.name = name;
            this.volume = volume;
        }

        public Ingredient toEntity(Recipe recipe) {
            return Ingredient.builder()
                    .num(num)
                    .name(name)
                    .volume(volume)
                    .recipe(recipe)
                    .build();
        }
    }

}
