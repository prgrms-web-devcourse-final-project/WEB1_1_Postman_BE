package postman.bottler.notification.exception;

import static postman.bottler.global.response.code.ErrorStatus.INVALID_NOTIFICATION_CREATION;
import static postman.bottler.global.response.code.ErrorStatus.NOTIFICATION_DUPLICATE_TOKEN;
import static postman.bottler.global.response.code.ErrorStatus.NOTIFICATION_NO_LETTER;
import static postman.bottler.global.response.code.ErrorStatus.NOTIFICATION_NO_TYPE;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;

@RestControllerAdvice
@Slf4j
public class NotificationExceptionHandler {

    @ExceptionHandler(InvalidNotificationRequestException.class)
    public ApiResponse<?> handleInvalidNotificationRequestException(InvalidNotificationRequestException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(INVALID_NOTIFICATION_CREATION.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(NoLetterIdException.class)
    public ApiResponse<?> handleNoLetterIdException(NoLetterIdException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(NOTIFICATION_NO_LETTER.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(NoTypeException.class)
    public ApiResponse<?> handleNoTypeException(NoTypeException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(NOTIFICATION_NO_TYPE.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(DuplicateTokenException.class)
    public ApiResponse<?> handleDuplicateTokenException(DuplicateTokenException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(NOTIFICATION_DUPLICATE_TOKEN.getCode(), e.getMessage(), null);
    }
}
