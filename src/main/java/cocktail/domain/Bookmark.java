package cocktail.domain;

import cocktail.domain.recipe.Recipe;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Bookmark {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public Bookmark(User user, Recipe recipe) {
        this.user = user;
        this.recipe = recipe;
        user.getBookmarks().add(this);
    }
}
