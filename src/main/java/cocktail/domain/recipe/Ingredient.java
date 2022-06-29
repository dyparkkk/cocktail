package cocktail.domain.recipe;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Ingredient {

    @Id
    @GeneratedValue
    private Long id;

    private int num;
    private String name;
    private String volume; // ex) 45ml

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Builder
    public Ingredient(int num, String name, String volume, Recipe recipe) {
        this.num = num;
        this.name = name;
        this.volume = volume;
        setRecipe(recipe);
    }

    private void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        recipe.getIngredients().add(this);
    }
}
