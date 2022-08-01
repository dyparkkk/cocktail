package cocktail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Data
public class UserProfileDto {
    private String loginName;
    private boolean loginUser;
    private boolean follow;
    private UserDto userDto;
    private int userFollowerCount;
    private int userFollowingCount;
}
