package cocktail.application.auth;

import cocktail.domain.User;
import lombok.Getter;

@Getter
public class SessionUser {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getNickname();
        this.email = user.getUsername();
//        this.picture = user.;
    }
}
