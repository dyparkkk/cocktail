package cocktail.application;

import cocktail.global.exception.DuplicateUserIdException;
import cocktail.global.exception.ErrorResponse;
import cocktail.global.exception.PwNotMatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler({DuplicateUserIdException.class, UsernameNotFoundException.class, PwNotMatchException.class})
    public ErrorResponse illegalExHandler(DuplicateUserIdException e) {
        log.error("[exceptionHandler] ex ", e);
        return ErrorResponse.createErrorResponse(false, "---", "invalid_parameter", e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ErrorResponse typeException(Exception e){
        log.info("[MissingServletRequestParameterException] ex ={}", e.getMessage());
        return ErrorResponse.createErrorResponse(false, "--"+ HttpStatus.BAD_REQUEST,
                "[MissingServletRequestParameterException]:파라미터의 문제가 있습니다. 타입이 맞지 않거나 생략되었을 수 있습니다. ",
                e.getMessage());
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ErrorResponse reqException(Exception e) {
        log.info("[ServletRequestBindingException] ex = {}", e.getMessage());
        return ErrorResponse.createErrorResponse(false, "--" + HttpStatus.BAD_REQUEST,
                "[ServletRequestBindingException]:바인딩 에러 ex) JSESSIONID 확인 불가 등",
                e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ErrorResponse illegalStateException(Exception e) {
        log.info("[IllegalStateException] ex = {}", e.getMessage());
        return ErrorResponse.createErrorResponse(false, "--" + HttpStatus.BAD_REQUEST,
                "[IllegalStateException]: 적절하지 않은 상태 ex) JSESSIONID 확인 불가 등",
                e.getMessage());
    }

    @ExceptionHandler
    public ErrorResponse exception(Exception e){
        return ErrorResponse.createErrorResponse(false, "--"+"???","오류 : 메시지를 읽어보세요", e.getMessage());
    }
}
