package postman.bottler.letter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.global.response.code.ErrorStatus;

@RestControllerAdvice
@Slf4j
public class LetterExceptionHandler {

    @ExceptionHandler(LetterNotFoundException.class)
    public ApiResponse<?> handleLetterNotFoundException(LetterNotFoundException e) {
        log.error("Letter not found: {}", e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.LETTER_NOT_FOUND.getCode(), e.getMessage(), null);
    }
}