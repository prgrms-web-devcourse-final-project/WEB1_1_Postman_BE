package postman.bottler.letter.exception;

import static postman.bottler.global.response.code.ErrorStatus.LETTER_ACCESS_DENIED;
import static postman.bottler.global.response.code.ErrorStatus.LETTER_ALREADY_SAVED;
import static postman.bottler.global.response.code.ErrorStatus.LETTER_NOT_FOUND;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;

@RestControllerAdvice
@Slf4j
public class LetterExceptionHandler {

    @ExceptionHandler(LetterNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleLetterNotFoundException(LetterNotFoundException e) {
        log.error("Letter not found: {}", e.getMessage());
        return ResponseEntity
                .status(LETTER_NOT_FOUND.getHttpStatus())
                .body(ApiResponse.onFailure(LETTER_NOT_FOUND.getCode(), e.getMessage(), null));
    }

    @ExceptionHandler(LetterAlreadySavedException.class)
    public ResponseEntity<ApiResponse<?>> handleLetterAlreadySavedException(LetterAlreadySavedException e) {
        log.error("Letter already saved: {}", e.getMessage());
        return ResponseEntity
                .status(LETTER_ALREADY_SAVED.getHttpStatus())
                .body(ApiResponse.onFailure(LETTER_ALREADY_SAVED.getCode(), e.getMessage(), null));
    }

    @ExceptionHandler(SavedLetterNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleSavedLetterNotFoundException(SavedLetterNotFoundException e) {
        log.error("Saved letter not found: {}", e.getMessage());
        return ResponseEntity
                .status(LETTER_NOT_FOUND.getHttpStatus())
                .body(ApiResponse.onFailure(LETTER_NOT_FOUND.getCode(), e.getMessage(), null));
    }

    @ExceptionHandler(LetterAccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(LetterAccessDeniedException e) {
        return ResponseEntity
                .status(LETTER_ACCESS_DENIED.getHttpStatus())
                .body(ApiResponse.onFailure(LETTER_ACCESS_DENIED.getCode(), e.getMessage(), null));
    }
}
