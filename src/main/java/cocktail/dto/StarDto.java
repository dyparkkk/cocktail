package cocktail.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class StarDto {

    @NotBlank(message = "rating is mandatory")
    private BigDecimal rating;

}
