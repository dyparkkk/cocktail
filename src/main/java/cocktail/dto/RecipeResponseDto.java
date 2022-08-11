package cocktail.dto;

import cocktail.domain.recipe.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

import static cocktail.dto.RecipeRequestDto.*;
import static java.util.stream.Collectors.*;

@Getter
@NoArgsConstructor
@SuperBuilder
public class RecipeResponseDto{
    private Long id;
    private String name;
    private BigDecimal star;
    private String writer; // 유저닉네임
    private List<String> tags;
    private Integer viewCnt;
    private Official official;
    private String createdDate;
    private String lastModifiedDate;
    private List<String> imageUrls;

    public static RecipeResponseDto fromEntity(Recipe recipe) {
        List<String> tagList = null;
        if (recipe.getTags() != null){
            tagList = recipe.getTags().stream()
                    .map(Tag::getName).collect(toList());
        }
        return new RecipeResponseDto(recipe.getId(), recipe.getName(), recipe.getStar(),
                recipe.getUser().getNickname(), tagList, recipe.getViewCnt(), recipe.getOfficial(),
                recipe.getCreatedDate().toString(), recipe.getLastModifiedDate().toString(),
                recipe.getImageUrls());
    }

    public RecipeResponseDto(Long id, String name, BigDecimal star, String writer, List<String> tags, Integer viewCnt, Official official, String createdDate, String lastModifiedDate, List<String> imageUrls) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.writer = writer;
        this.tags = tags;
        this.viewCnt = viewCnt;
        this.official = official;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.imageUrls = imageUrls;
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
        private String garnish;
        private int voter;
        private boolean myRecipe;

        public static DetailDto from(Recipe recipe) {
            return DetailDto.builder()
                    .id(recipe.getId())
                    .name(recipe.getName())
                    .dosu(recipe.getDosu() == null ? null : recipe.getDosu().toString())
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
                    .garnish(recipe.getGarnish())
                    .createdDate(recipe.getCreatedDate().toString())
                    .lastModifiedDate(recipe.getLastModifiedDate().toString())
                    .star(recipe.getStar())
                    .writer(recipe.getUser().getNickname())
                    .viewCnt(recipe.getViewCnt())
                    .imageUrls(recipe.getImageUrls())
                    .official(recipe.getOfficial())
                    .voter(recipe.getVoter())
                    .myRecipe(false)
                    .build();
        }

        public DetailDto(Long id, String name, BigDecimal star, String writer, List<String> tags, Integer viewCnt, Official official, String createdDate, String lastModifiedDate, List<String> imageUrls, String dosu, Brewing brewing, Base base, List<Order> orders, List<IngredientDto> ingredients, String glass, Integer soft, Integer sweet, String garnish, boolean myRecipe) {
            super(id, name, star, writer, tags, viewCnt, official, createdDate, lastModifiedDate, imageUrls);
            this.dosu = dosu;
            this.brewing = brewing;
            this.base = base;
            this.orders = orders;
            this.ingredients = ingredients;
            this.glass = glass;
            this.soft = soft;
            this.sweet = sweet;
            this.garnish = garnish;
            this.myRecipe = myRecipe;
        }

        public void setMyRecipe(boolean myRecipe){
            this.myRecipe = myRecipe;
        }
    }


}
