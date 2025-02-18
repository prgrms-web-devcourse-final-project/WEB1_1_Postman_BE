package postman.bottler.label.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.global.response.code.ErrorStatus;

@RestControllerAdvice
@Slf4j
public class LabelExceptionHandler {
    @ExceptionHandler(InvalidLabelException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleInvalidLabelException(InvalidLabelException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_LABEL.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(EmptyLabelInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleEmptyLabelInputException(EmptyLabelInputException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.EMPTY_LABEL_INPUT.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(UserLabelNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleUserLabelNotFoundException(UserLabelNotFoundException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.USER_LABEL_NOT_FOUND.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(FirstComeFirstServedLabelException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleFirstComeFirstServedLabelException(FirstComeFirstServedLabelException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.USER_LABEL_NOT_FOUND.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(DuplicateLabelException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleDuplicateLabelException(DuplicateLabelException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.DUPLICATE_LABEL.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(LabelRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleLabelRequestException(LabelRequestException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.DUPLICATE_LABEL.getCode(), e.getMessage(), null);
    }
}
