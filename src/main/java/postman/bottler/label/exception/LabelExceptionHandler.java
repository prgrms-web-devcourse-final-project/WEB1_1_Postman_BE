package postman.bottler.label.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.global.response.code.ErrorStatus;

@RestControllerAdvice
@Slf4j
public class LabelExceptionHandler {
    @ExceptionHandler(InvalidLabelException.class)
    public ApiResponse<?> handleEmptyLabelException(InvalidLabelException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_LABEL.getCode(), e.getMessage(), null);
    }
}
