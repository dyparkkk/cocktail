package cocktail.dto;

import cocktail.domain.recipe.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class RecipeRequestDto {

    private String name;

    // 검증 xx.x 형식
    @ApiModelProperty (name = "도수",dataType = "string", required = true)
    private BigDecimal dosu;

    @ApiModelProperty(name = "조주법", dataType = "Brewing", required = true)
    private Brewing brewing;

    @ApiModelProperty(name = "기주", dataType = "Base", required = true)
    private Base base;

    @ApiModelProperty(name = "태그", dataType = "String 배열")
    private List<String> tags;

    @ApiModelProperty(name = "조리법 순서", dataType = "배열")
    private List<OrderDto> orders;

    @ApiModelProperty(name = "재료", dataType = "배열")
    private List<IngredientDto> ingredients;

    @Builder
    public RecipeRequestDto(String name, BigDecimal dosu, Brewing brewing, Base base,
                            List<String> tags, List<OrderDto> orders, List<IngredientDto> ingredients) {
        this.name = name;
        this.dosu = dosu;
        this.brewing = brewing;
        this.base = base;
        this.tags = tags;
        this.orders = orders;
        this.ingredients = ingredients;
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

        @ApiModelProperty(name = "번호", example = "1")
        private int num;

        @ApiModelProperty(name = "내용")
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
        @ApiModelProperty(name = "번호", example = "1")
        private int num;

        @ApiModelProperty(name = "재료이름")
        private String name;

        @ApiModelProperty(name = "용량 ex) 45ml, 1/2oz 등")
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
