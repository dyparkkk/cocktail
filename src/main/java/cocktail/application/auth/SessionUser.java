package cocktail.application.auth;

import cocktail.domain.user.User;
import cocktail.dto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@NoArgsConstructor
public class SessionUser implements Serializable {
    private String nickname;
    private String username;

    public SessionUser(User user) {
        this.nickname = user.getNickname();
        this.username = user.getUsername();
    }

    public SessionUser(UserDto dto) {
        this.nickname = dto.getNickname();
        this.username = dto.getUsername();
    }
}
