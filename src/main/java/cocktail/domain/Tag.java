package cocktail.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Tag {
    
    @Id @GeneratedValue
    private Long id;
}
