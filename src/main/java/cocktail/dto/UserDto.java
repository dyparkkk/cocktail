package cocktail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpRequestDto {

        @Email
        @NotBlank
        private String username;

        @NotBlank
        private String pw;
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
