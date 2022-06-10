package cocktail.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "recipe_tag")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class RecipeTag {
    @Id
    @GeneratedValue
    @Column(name = "recipe_tag_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}
