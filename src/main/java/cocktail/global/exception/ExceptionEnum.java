package cocktail.application;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ExceptionEnum {

    //409 CONFLICT 중복 리소스
    ID_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),

    //404 NOT_FOUND 잘못된 리소스 접근
    LOGIN_FAIL(HttpStatus.NOT_FOUND, "해당 계정을 찾을 수 없습니다. 아이디와 비밀번호를 확인해주세요.");

    private final HttpStatus httpStatus;
    private String message;

    ExceptionEnum(HttpStatus status) {
        this.httpStatus = status;
    }

    ExceptionEnum(HttpStatus status, String message) {
        this.httpStatus = status;
        this.message = message;
    }
}
