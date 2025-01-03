package postman.bottler.notification.exception;

import static postman.bottler.global.response.code.ErrorStatus.INVALID_NOTIFICATION_CREATION;
import static postman.bottler.global.response.code.ErrorStatus.NOTIFICATION_DUPLICATE_TOKEN;
import static postman.bottler.global.response.code.ErrorStatus.NOTIFICATION_NO_LETTER;
import static postman.bottler.global.response.code.ErrorStatus.NOTIFICATION_NO_TYPE;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;

@RestControllerAdvice
@Slf4j
public class NotificationExceptionHandler {

    @ExceptionHandler(InvalidNotificationRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleInvalidNotificationRequestException(InvalidNotificationRequestException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(INVALID_NOTIFICATION_CREATION.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(NoLetterIdException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleNoLetterIdException(NoLetterIdException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(NOTIFICATION_NO_LETTER.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(NoTypeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleNoTypeException(NoTypeException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(NOTIFICATION_NO_TYPE.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(DuplicateTokenException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleDuplicateTokenException(DuplicateTokenException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(NOTIFICATION_DUPLICATE_TOKEN.getCode(), e.getMessage(), null);
    }
}
