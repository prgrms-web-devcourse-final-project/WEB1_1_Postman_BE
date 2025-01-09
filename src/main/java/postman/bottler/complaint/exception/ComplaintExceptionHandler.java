package postman.bottler.complaint.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.global.response.code.ErrorStatus;

@RestControllerAdvice
@Slf4j
public class ComplaintExceptionHandler {

    @ExceptionHandler(InvalidComplainException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleInvalidComplainException(InvalidComplainException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_COMPLAINT.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(DuplicateComplainException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleDuplicateComplainException(DuplicateComplainException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.DUPLICATE_COMPLAINT.getCode(), e.getMessage(), null);
    }
}
