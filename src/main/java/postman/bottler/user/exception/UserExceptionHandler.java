package postman.bottler.user.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.global.response.code.ErrorStatus;

@RestControllerAdvice
@Slf4j
public class UserExceptionHandler {
    @ExceptionHandler(EmailException.class)
    public ApiResponse<?> handleEmailException(EmailException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_EMAIL.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(PasswordException.class)
    public ApiResponse<?> handlePasswordException(PasswordException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_PASSWORD.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(NicknameException.class)
    public ApiResponse<?> handleNicknameException(NicknameException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_NICKNAME.getCode(), e.getMessage(), null);
    }
}
