package cocktail.dto;

import cocktail.domain.Base;
import cocktail.domain.Brewing;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SearchCondition {
    private Brewing brewing;
    private Base base;
    private List<String> tagList;
    private String name;

    public void setBrewing(String brewing) {
        this.brewing = Brewing.valueOf(brewing.toUpperCase());
    }
    public void setBase(String base){
        this.base = Base.valueOf(base.toUpperCase());
    }

    @Builder
    public SearchCondition(Brewing brewing, Base base, List<String> tagList, String name) {
        this.brewing = brewing;
        this.base = base;
        this.tagList = tagList;
        this.name = name;
    }
}
