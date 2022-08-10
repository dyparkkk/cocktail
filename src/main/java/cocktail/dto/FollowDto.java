package cocktail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class FollowDto {

    private Long id;
    private String nickname;
    private String profileImgUrl;
    private BigInteger followState;
    private BigInteger equalState;
    private int loginUser;


}
