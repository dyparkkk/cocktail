package cocktail.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends IllegalAccessException {

    private final ExceptionEnum error;

    public CustomException(ExceptionEnum e) {
        super(e.getMessage());
        this.error = e;
    }
}
