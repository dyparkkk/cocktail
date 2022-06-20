package cocktail.domain.recipe;

import cocktail.domain.recipe.Recipe;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Tag {
    
    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public Tag(String name, Recipe recipe) {
        this.name = name;
        setRecipe(recipe);
    }

    private void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        recipe.getTags().add(this);
    }
}
