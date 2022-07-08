package cocktail.dto;

import cocktail.domain.recipe.Base;
import cocktail.domain.recipe.Brewing;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SearchCondition {
    private Brewing brewing;
    private Base base;
    private List<String> tagList;
    private String name;

    public void setBrewing(String brewing) {
        if (StringUtils.hasText(brewing)){
            this.brewing = Brewing.valueOf(brewing.toUpperCase());
        }
    }
    public void setBase(String base){
        if (StringUtils.hasText(base)) {
            this.base = Base.valueOf(base.toUpperCase());
        }
    }

    @Builder
    public SearchCondition(Brewing brewing, Base base, List<String> tagList, String name) {
        this.brewing = brewing;
        this.base = base;
        this.tagList = tagList;
        this.name = name;
    }
}
