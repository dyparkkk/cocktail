package cocktail.domain.recipe;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Tag {
    
    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public Tag(String name, Recipe recipe) {
        this.name = name;
        setRecipe(recipe);
    }

    public String getName() {
        return name;
    }

    private void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        recipe.getTags().add(this);
    }
}
