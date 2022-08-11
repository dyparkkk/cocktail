package cocktail.dto;

import cocktail.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowDto {

    private String username;
    private String nickname;
    private String profileImgUrl;
    private boolean myFollowing;

    public static FollowDto fromUser(User user) {
        return new FollowDto(user);
    }
    private FollowDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.profileImgUrl = user.getProfileImgUrl();
        this.myFollowing = false;
    }

    public void setFollowing(boolean following) {
        myFollowing = following;
    }
}
