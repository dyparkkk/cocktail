package cocktail.domain.recipe;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Star {

    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private long recipeId;

    private BigDecimal rating;

    public Star(String username, long recipeId, BigDecimal rating) {
        this.username = username;
        this.recipeId = recipeId;
        this.rating = rating;
    }
}
