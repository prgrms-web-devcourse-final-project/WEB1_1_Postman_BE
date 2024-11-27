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
    public ApiResponse<?> handleInvalidLabelException(InvalidLabelException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_LABEL.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(EmptyLabelInputException.class)
    public ApiResponse<?> handleEmptyLabelInputException(EmptyLabelInputException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.EMPTY_LABEL_INPUT.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(UserLabelNotFoundException.class)
    public ApiResponse<?> handleUserLabelNotFoundException(UserLabelNotFoundException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.USER_LABEL_NOT_FOUND.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(FirstComeFirstServedLabelException.class)
    public ApiResponse<?> handleFirstComeFirstServedLabelException(FirstComeFirstServedLabelException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.USER_LABEL_NOT_FOUND.getCode(), e.getMessage(), null);
    }
}
