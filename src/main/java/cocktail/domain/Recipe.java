package cocktail.domain;

import cocktail.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Recipe extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal dosu;

    // orders는 @ElementCollection, @CollectionTable 사용 예정

}
