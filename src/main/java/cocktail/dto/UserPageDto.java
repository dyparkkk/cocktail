package cocktail.dto;

import cocktail.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPageDto {
    private String nickName;
    private String profileUrl;
    private int recipeNum;
    private int followingNum;
    private int followerNum;
    private String title;
    private boolean myFollow;

    public static UserPageDto from(User user) {
        return UserPageDto.builder()
                .nickName(user.getNickname())
                .profileUrl(user.getProfileImgUrl())
                .title(user.getTitle())
                .followerNum(user.getFollowerNum())
                .followingNum(user.getFollowingNum())
                .recipeNum(user.getMyRecipeNum())
                .myFollow(false)
                .build();
    }

    @Builder
    private UserPageDto(String nickName, String profileUrl, int recipeNum, int followingNum, int followerNum, String title, boolean myFollow) {
        this.nickName = nickName;
        this.profileUrl = profileUrl;
        this.recipeNum = recipeNum;
        this.followingNum = followingNum;
        this.followerNum = followerNum;
        this.title = title;
        this.myFollow = myFollow;
    }

    public void setMyFollow(boolean myFollow) {
        this.myFollow = myFollow;
    }
}
