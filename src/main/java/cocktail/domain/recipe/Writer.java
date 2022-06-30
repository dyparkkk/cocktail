package cocktail.domain.recipe;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Writer {

    private Long userId;

    @Column(name = "writer_username")
    private String username;

    @Column(name = "writer_nickname")
    private String nickname;

    public Writer(Long userId, String username, String nickname) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
    }
}
