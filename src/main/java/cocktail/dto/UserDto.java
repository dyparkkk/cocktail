package cocktail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserDto {

    @Email
    @NotBlank
    private Long userId;

    @NotBlank
    private String username;

    @NotBlank
    private String nickname;

    public UserDto(Long userId, String username, String nickname) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
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
        private String roles;

        public SignUpRequestDto(String username, String pw, String nickname, String roles) {
            this.username = username;
            this.pw = pw;
            this.nickname = nickname;
            this.roles = roles;
        }
    }


    @Getter
    @NoArgsConstructor
    public static class LoginRequestDto{
        @Email
        @NotBlank
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
}
