package cocktail.dto;

import cocktail.domain.recipe.*;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cocktail.dto.RecipeRequestDto.*;
import static java.util.stream.Collectors.*;

@Getter
@NoArgsConstructor
@SuperBuilder
public class RecipeResponseDto {
    private Long id;
    private String name;
    private BigDecimal star;
    private String writer; // 유저닉네임
    private List<String> tags;
    private Integer viewCnt;
    private String createdDate;
    private String lastModifiedDate;
    // 추가예정 - 사진
    // official 여부

    public static RecipeResponseDto fromEntity(Recipe recipe) {
        List<String> tagList = recipe.getTags().stream()
                .map(t -> t.getName()).collect(toList());
        return new RecipeResponseDto(recipe.getId(), recipe.getName(), recipe.getStar(),
                recipe.getUser().getNickname(), tagList, recipe.getViewCnt(),
                recipe.getCreatedDate().toString(), recipe.getLastModifiedDate().toString());
    }

    public RecipeResponseDto(Long id, String name, BigDecimal star, String writer, List<String> tags, Integer viewCnt, String createdDate, String lastModifiedDate) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.writer = writer;
        this.tags = tags;
        this.viewCnt = viewCnt;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    @Getter
    @NoArgsConstructor
    @SuperBuilder
    public static class DetailDto extends RecipeResponseDto{
        private String dosu;
        private Brewing brewing;
        private Base base;
        private List<Order> orders;
        private List<IngredientDto> ingredients;
        private String glass;
        private Integer soft;
        private Integer sweet;
        private Set<String> garnishes;

        public static DetailDto from(Recipe recipe) {
            return DetailDto.builder()
                    .id(recipe.getId())
                    .name(recipe.getName())
                    .dosu(recipe.getDosu().toString())
                    .brewing(recipe.getBrewing())
                    .base(recipe.getBase())
                    .orders(recipe.getOrders())
                    .tags(recipe.getTags().stream()
                            .map(Tag::getName)
                            .collect(toList())
                    )
                    .ingredients(recipe.getIngredients().stream()
                            .map(IngredientDto::fromEntity)
                            .collect(toList()))
                    .glass(recipe.getGlass())
                    .soft(recipe.getSoft())
                    .sweet(recipe.getSweet())
                    .garnishes(recipe.getGarnishes())
                    .createdDate(recipe.getCreatedDate().toString())
                    .lastModifiedDate(recipe.getLastModifiedDate().toString())
                    .star(recipe.getStar())
                    .writer(recipe.getUser().getNickname())
                    .viewCnt(recipe.getViewCnt())
                    .build();
        }

        public DetailDto(Long id, String name, BigDecimal star, String writer, List<String> tags, Integer viewCnt, String createdDate, String lastModifiedDate, String dosu, Brewing brewing, Base base, List<Order> orders, List<IngredientDto> ingredients, String glass, Integer soft, Integer sweet, Set<String> garnishes) {
            super(id, name, star, writer, tags, viewCnt, createdDate, lastModifiedDate);
            this.dosu = dosu;
            this.brewing = brewing;
            this.base = base;
            this.orders = orders;
            this.ingredients = ingredients;
            this.glass = glass;
            this.soft = soft;
            this.sweet = sweet;
            this.garnishes = garnishes;
        }
    }


}
