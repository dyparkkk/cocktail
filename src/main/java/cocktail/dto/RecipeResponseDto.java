package cocktail.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class RecipeResponseDto {
    private String name;

    // 추가예정
    //사진
//    private String userNickname;
    // 별점, 뷰카운트

    public static class RecipeListDto {
        private String name;

        @QueryProjection
        public RecipeListDto(String name) {
            this.name = name;
        }
    }
}
