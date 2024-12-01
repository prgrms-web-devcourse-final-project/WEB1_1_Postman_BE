package postman.bottler.letter.exception;

import static postman.bottler.global.response.code.ErrorStatus.INVALID_SORT_FIELD;
import static postman.bottler.global.response.code.ErrorStatus.LETTER_ACCESS_DENIED;
import static postman.bottler.global.response.code.ErrorStatus.LETTER_ALREADY_SAVED;
import static postman.bottler.global.response.code.ErrorStatus.LETTER_NOT_FOUND;
import static postman.bottler.global.response.code.ErrorStatus.LETTER_UNKNOWN_VALIDATION_ERROR;
import static postman.bottler.global.response.code.ErrorStatus.LETTER_VALIDATION_ERROR;
import static postman.bottler.global.response.code.ErrorStatus.PAGINATION_VALIDATION_ERROR;
import static postman.bottler.global.response.code.ErrorStatus.REPLY_LETTER_VALIDATION_ERROR;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.global.response.code.ErrorStatus;

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

    @ExceptionHandler(BaseLetterValidationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            BaseLetterValidationException e) {
        ErrorStatus status;

        if (e instanceof InvalidLetterRequestException) {
            status = LETTER_VALIDATION_ERROR;
        } else if (e instanceof InvalidReplyLetterRequestException) {
            status = REPLY_LETTER_VALIDATION_ERROR;
        } else if (e instanceof InvalidPageRequestException) {
            status = PAGINATION_VALIDATION_ERROR;
        } else {
            status = LETTER_UNKNOWN_VALIDATION_ERROR; // 기본 처리
        }

        log.error("{}: {}", status, e.getErrors());

        return ResponseEntity
                .status(status.getHttpStatus())
                .body(
                        ApiResponse.onFailure(
                                status.getCode(),
                                e.getMessage(), // ErrorStatus 메시지 사용
                                e.getErrors()
                        )
                );
    }

    @ExceptionHandler(InvalidSortFieldException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidSortFieldException(InvalidSortFieldException e) {
        return ResponseEntity
                .status(INVALID_SORT_FIELD.getHttpStatus())
                .body(ApiResponse.onFailure(INVALID_SORT_FIELD.getCode(), e.getMessage(), null));
    }
}
