package cocktail.dto;

import cocktail.domain.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Getter
@NoArgsConstructor
@SuperBuilder
public class UserDto {

    @Email
    @NotBlank
    private String username;

    @NotBlank
    private String nickname;

    private String title;
    private String profileImgUrl;

    public UserDto(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }

    public static UserDto from(User user){
        return UserDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .title(user.getTitle())
                .profileImgUrl(user.getProfileImgUrl())
                .build();
    }

    @Getter
    @NoArgsConstructor
    public static class SignUpRequestDto{
        @Email
        @NotBlank
        private String username;
        @NotBlank
        private String pw;
        @NotBlank
        private String nickname;

        private String title;
        private String profileImgUrl;
        private String roles;

        public SignUpRequestDto(String username, String pw, String nickname,String title,String profileImgUrl, String roles) {
            this.username = username;
            this.pw = pw;
            this.nickname = nickname;
            this.title = title;
            this.profileImgUrl = profileImgUrl;
            this.roles = roles;
        }
    }


    @Getter
    @NoArgsConstructor
    public static class LoginRequestDto{
        @Email
        @NotBlank
        @ApiModelProperty()
        private String username;
        @NotBlank
        private String pw;

        public LoginRequestDto(String username, String pw) {
            this.username = username;
            this.pw = pw;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserIdCheckDto{
        @Email
        @NotBlank
        private String username;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NicknameCheckDto{
        @NotBlank
        private String nickname;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserUpdateDto{
        private Long id;
        private String pw;
        private String  nickname;
        private String title;
        private String profileImgUrl;
    }

}
