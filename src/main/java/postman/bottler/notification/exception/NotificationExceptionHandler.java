package postman.bottler.notification.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.global.response.code.ErrorStatus;

@RestControllerAdvice
@Slf4j
public class NotificationExceptionHandler {

    @ExceptionHandler(InvalidNotificationRequestException.class)
    public ApiResponse<?> handleInvalidNotificationRequestException(InvalidNotificationRequestException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_NOTIFICATION_CREATION.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(NoLetterIdException.class)
    public ApiResponse<?> handleNoLetterIdException(NoLetterIdException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.NOTIFICATION_NO_LETTER.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(NoTypeException.class)
    public ApiResponse<?> handleNoTypeException(NoTypeException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.NOTIFICATION_NO_TYPE.getCode(), e.getMessage(), null);
    }
}
