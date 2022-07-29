package cocktail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
@Data
public class FollowDto {

    private Long id;
    private String nickname;
    private String profileImgUrl;
    private int followState;
    private int loginUser;

    public FollowDto(Integer id, String nickname,String profileImgUrl,int followState,int loginUser){
        this.id = id.longValue();
        this.nickname= nickname;
        this.profileImgUrl = profileImgUrl;
        this.followState = followState;
        this.loginUser= loginUser;

    }

}
