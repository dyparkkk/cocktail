package cocktail.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "images")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Image {

    @Id @GeneratedValue
    private Long id;
}
