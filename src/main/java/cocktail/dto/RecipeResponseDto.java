package cocktail.dto;

import cocktail.domain.recipe.*;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.*;

@Getter
@NoArgsConstructor
public class RecipeResponseDto {
    private Long id;
    private String name;
    // 추가예정
    //사진
//    private String userNickname;
    // 별점, 뷰카운트

    public RecipeResponseDto(Long id) {
        this.id = id;
    }

    @QueryProjection
    public RecipeResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Getter
    @NoArgsConstructor
    public static class DetailDto extends RecipeResponseDto{
        private String dosu;
        private Brewing brewing;
        private Base base;
        private List<String> tags;
        private List<Order> orders;

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
                    .build();
        }

        @Builder
        public DetailDto(Long id, String name, String dosu, Brewing brewing, Base base, List<String> tags, List<Order> orders) {
            super(id, name);
            this.dosu = dosu;
            this.brewing = brewing;
            this.base = base;
            this.tags = tags;
            this.orders = orders;
        }
    }


}
