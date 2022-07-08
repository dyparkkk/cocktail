package cocktail.dto;

import cocktail.domain.recipe.*;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cocktail.dto.RecipeRequestDto.*;
import static java.util.stream.Collectors.*;

@Getter
@NoArgsConstructor
public class RecipeResponseDto {
    private Long id;
    private String name;
    private String star;
    private String writer; // 유저닉네임
    private List<String> tags;
    private String createdDate;
    private String lastModifiedDate;
    // 추가예정 - 사진

    public static RecipeResponseDto fromEntity(Recipe recipe) {
        List<String> tagList = recipe.getTags().stream()
                .map(t -> t.getName()).collect(toList());
        return new RecipeResponseDto(recipe.getId(), recipe.getName(), recipe.getStar(),
                recipe.getUser().getNickname(), tagList,
                recipe.getCreatedDate().toString(), recipe.getLastModifiedDate().toString());
    }

    @Builder
    public RecipeResponseDto(Long id, String name, String star, String writer, List<String> tags, String createdDate, String lastModifiedDate) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.writer = writer;
        this.tags = tags;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    @Getter
    @NoArgsConstructor
    public static class DetailDto{
        private Long id;
        private String name;
        private String dosu;
        private Brewing brewing;
        private Base base;
        private List<String> tags;
        private List<Order> orders;
        private List<Ingredient> ingredients;
        private String glass;
        private Integer soft;
        private Integer sweet;
        private Set<String> garnishes;
        private String createdDate;
        private String lastModifiedDate;

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
                    .ingredients(recipe.getIngredients())
                    .glass(recipe.getGlass())
                    .soft(recipe.getSoft())
                    .sweet(recipe.getSweet())
                    .garnishes(recipe.getGarnishes())
                    .createdDate(recipe.getCreatedDate().toString())
                    .lastModifiedDate(recipe.getLastModifiedDate().toString())
                    .build();
        }

        @Builder
        public DetailDto(Long id, String name, String dosu, Brewing brewing, Base base, List<String> tags, List<Order> orders, List<Ingredient> ingredients, String glass, Integer soft, Integer sweet, Set<String> garnishes, String createdDate, String lastModifiedDate) {
            this.id = id;
            this.name = name;
            this.dosu = dosu;
            this.brewing = brewing;
            this.base = base;
            this.tags = tags;
            this.orders = orders;
            this.ingredients = ingredients;
            this.glass = glass;
            this.soft = soft;
            this.sweet = sweet;
            this.garnishes = garnishes;
            this.createdDate = createdDate;
            this.lastModifiedDate = lastModifiedDate;
        }
    }


}
