package cocktail.global.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private boolean success;
    private Error error;

    public static ErrorResponse createErrorResponse(boolean success, String code, String message, String content) {
        Error error = new Error(code, message, content);
        return new ErrorResponse(success, error);
    }

    @Data
    @AllArgsConstructor
    private static class Error{
        private String code;
        private String message;
        private String content;
    }
}
