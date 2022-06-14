package cocktail.global.exception;

public class DuplicateUserIdException extends RuntimeException {
    public DuplicateUserIdException() {
        super();
    }

    public DuplicateUserIdException(String message) {
        super(message);
    }

    public DuplicateUserIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
