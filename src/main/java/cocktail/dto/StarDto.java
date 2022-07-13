package cocktail.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class StarDto {
    @NotNull
    private long RecipeId;

    private BigDecimal rating;


}
