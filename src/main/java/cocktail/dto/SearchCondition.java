package cocktail.dto;

import cocktail.domain.recipe.Base;
import cocktail.domain.recipe.Brewing;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SearchCondition {
    private Brewing brewing;
    private Base base;
    private List<String> tagList;
    private String name;
    private List<String> ingredientList;

    @DecimalMin("0")
    private BigDecimal leastDosu;

    @DecimalMax("100")
    private BigDecimal maxDosu;


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
    public SearchCondition(Brewing brewing, Base base, List<String> tagList, String name, List<String> ingredientList, BigDecimal leastDosu, BigDecimal maxDosu) {
        this.brewing = brewing;
        this.base = base;
        this.tagList = tagList;
        this.name = name;
        this.ingredientList = ingredientList;
        this.leastDosu = leastDosu;
        this.maxDosu = maxDosu;
    }
}
