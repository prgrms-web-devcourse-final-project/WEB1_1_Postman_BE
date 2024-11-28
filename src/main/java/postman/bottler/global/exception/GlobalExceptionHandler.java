package postman.bottler.global.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.global.response.code.ErrorStatus;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        log.error("유효성 검사 실패: {}", errors);

        return ResponseEntity
                .status(ErrorStatus.VALIDATION_ERROR.getHttpStatus())
                .body(
                        ApiResponse.onFailure(
                                ErrorStatus.VALIDATION_ERROR.getCode(),
                                "유효성 검사에 실패했습니다.",
                                errors
                        )
                );
    }
}
