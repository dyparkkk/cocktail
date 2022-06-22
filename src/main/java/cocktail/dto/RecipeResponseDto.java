package cocktail.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
