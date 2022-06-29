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

    private Long userId;
    private String username;
    private String nickname;

    public UserDto(Long userId, String username, String nickname) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpRequestDto {

        @Email
        @NotBlank
        private String username;

        @NotBlank
        private String pw;

        @NotBlank
        private String nickname;

        private String roles;
    }

    @Data
    @AllArgsConstructor
    public static class SuccessResponseDto{
        private final Boolean success = true;
    }

    @Data
    @NoArgsConstructor
    public static class SignUpResponseDto {
        private final Boolean success = true;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginRequestDto{
        @Email
        @NotBlank
        private String username;
        @NotBlank
        private String pw;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserIdCheckDto{
        @Email
        @NotBlank
        private String username;
    }
}
