package postman.bottler.global.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.global.response.ErrorStatus;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonForbiddenException.class)
    public ApiResponse<?> handleCommonForbiddenException(CommonForbiddenException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.FORBIDDEN.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ApiResponse<?> handleInvalidLoginException(InvalidLoginException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_LOGIN.getCode(), e.getMessage(), null);
    }
}
