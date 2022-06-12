package cocktail.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RecipeRequestDto {
    private String name;
    // 검증 xx.x 형식
    private String dosu;
    private List<String> tags;
    private List<String> orders;

    public RecipeRequestDto(String name, String dosu, List<String> tags) {
        this.name = name;
        this.dosu = dosu;
        this.tags = tags;
    }
}
