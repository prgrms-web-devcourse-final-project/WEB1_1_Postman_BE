package postman.bottler.complaint.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.global.response.code.ErrorStatus;

@RestControllerAdvice
@Slf4j
public class ComplaintExceptionHandler {

    @ExceptionHandler(InvalidComplainException.class)
    public ApiResponse<?> handleInvalidComplainException(InvalidComplainException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_COMPLAINT.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(DuplicateComplainException.class)
    public ApiResponse<?> handleDuplicateComplainException(DuplicateComplainException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.DUPLICATE_COMPLAINT.getCode(), e.getMessage(), null);
    }
}
