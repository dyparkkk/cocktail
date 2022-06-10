package cocktail.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class User {

    @Id @GeneratedValue
    @Column(name = "users_id")
    private Long id;
    private String username; // userId
    private String pw;

}
