package cocktail.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecipeResponseDto {
    private Long id;
    // 추가예정
    //사진
//    private String userNickname;
    // 별점, 뷰카운트


    public RecipeResponseDto(Long id) {
        this.id = id;
    }

    @Getter
    @NoArgsConstructor
    public static class RecipeListDto {
        private String name;

        @QueryProjection
        public RecipeListDto(String name) {
            this.name = name;
        }
    }
}
