package cocktail.domain;

import cocktail.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Recipe extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private BigDecimal dosu;

    @OneToMany(mappedBy = "Tag")
    private List<Tag> tags  = new ArrayList<>();


    // orders는 @ElementCollection, @CollectionTable 사용 예정


    public Recipe(String name, BigDecimal dosu) {
        this.name = name;
        this.dosu = dosu;
    }

    public List<Tag> getTags() {
        return tags;
    }
}
