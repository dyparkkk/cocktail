package cocktail.application.auth;

import cocktail.domain.User;
import cocktail.dto.UserDto;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@ToString
public class SessionUser implements Serializable {
    private Long id;
    private String name;
    private String email;

    public SessionUser(User user) {
        this.id = user.getId();
        this.name = user.getNickname();
        this.email = user.getUsername();
    }

    public SessionUser(UserDto dto) {
        this.id = dto.getUserId();
        this.name = dto.getNickname();
        this.email = dto.getUsername();
    }
}
