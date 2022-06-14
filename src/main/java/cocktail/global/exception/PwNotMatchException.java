package cocktail.global.exception;

public class PwNotMatchException extends RuntimeException{
    public PwNotMatchException() {
        super();
    }

    public PwNotMatchException(String message) {
        super(message);
    }

    public PwNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PwNotMatchException(Throwable cause) {
        super(cause);
    }

    protected PwNotMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

