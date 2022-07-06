package cocktail.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkDto {
    private String username;
    private Long recipeId;

    public BookmarkDto(String username, Long recipeId) {
        this.username = username;
        this.recipeId = recipeId;
    }
}
