package postman.bottler.letter.exception;

import static postman.bottler.global.response.code.ErrorStatus.DUPLICATE_REPLY_LETTER;
import static postman.bottler.global.response.code.ErrorStatus.INVALID_LETTER_TYPE;
import static postman.bottler.global.response.code.ErrorStatus.INVALID_SORT_FIELD;
import static postman.bottler.global.response.code.ErrorStatus.LETTER_ACCESS_DENIED;
import static postman.bottler.global.response.code.ErrorStatus.LETTER_ALREADY_SAVED;
import static postman.bottler.global.response.code.ErrorStatus.LETTER_AUTHOR_MISMATCH;
import static postman.bottler.global.response.code.ErrorStatus.LETTER_NOT_FOUND;
import static postman.bottler.global.response.code.ErrorStatus.TEMP_RECOMMENDATIONS_NOT_FOUND;
import static postman.bottler.global.response.code.ErrorStatus.UNAUTHORIZED_LETTER_ACCESS;

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
                .header("Content-Type", "application/json")
                .body(ApiResponse.onFailure(LETTER_NOT_FOUND.getCode(), e.getMessage(), null));
    }

    @ExceptionHandler(LetterAlreadySavedException.class)
    public ResponseEntity<ApiResponse<?>> handleLetterAlreadySavedException(LetterAlreadySavedException e) {
        log.error("Letter already saved: {}", e.getMessage());
        return ResponseEntity
                .status(LETTER_ALREADY_SAVED.getHttpStatus())
                .header("Content-Type", "application/json")
                .body(ApiResponse.onFailure(LETTER_ALREADY_SAVED.getCode(), e.getMessage(), null));
    }

    @ExceptionHandler(SavedLetterNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleSavedLetterNotFoundException(SavedLetterNotFoundException e) {
        log.error("Saved letter not found: {}", e.getMessage());
        return ResponseEntity
                .status(LETTER_NOT_FOUND.getHttpStatus())
                .header("Content-Type", "application/json")
                .body(ApiResponse.onFailure(LETTER_NOT_FOUND.getCode(), e.getMessage(), null));
    }

    @ExceptionHandler(LetterAccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(LetterAccessDeniedException e) {
        return ResponseEntity
                .status(LETTER_ACCESS_DENIED.getHttpStatus())
                .header("Content-Type", "application/json")
                .body(ApiResponse.onFailure(LETTER_ACCESS_DENIED.getCode(), e.getMessage(), null));
    }

    @ExceptionHandler(DuplicateReplyLetterException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(DuplicateReplyLetterException e) {
        return ResponseEntity
                .status(DUPLICATE_REPLY_LETTER.getHttpStatus())
                .header("Content-Type", "application/json")
                .body(ApiResponse.onFailure(DUPLICATE_REPLY_LETTER.getCode(), e.getMessage(), null));
    }

    @ExceptionHandler(LetterValidationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            LetterValidationException e) {

        log.error("{}: {}", e.getErrorStatus(), e.getErrors());

        return ResponseEntity
                .status(e.getErrorStatus().getHttpStatus())
                .header("Content-Type", "application/json")
                .body(
                        ApiResponse.onFailure(
                                e.getErrorStatus().getCode(),
                                e.getMessage(), // ErrorStatus 메시지 사용
                                e.getErrors()
                        )
                );
    }

    @ExceptionHandler(InvalidSortFieldException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidSortFieldException(InvalidSortFieldException e) {
        return ResponseEntity
                .status(INVALID_SORT_FIELD.getHttpStatus())
                .header("Content-Type", "application/json")
                .body(ApiResponse.onFailure(INVALID_SORT_FIELD.getCode(), e.getMessage(), null));
    }

    @ExceptionHandler(InvalidLetterTypeException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidLetterTypeException(InvalidLetterTypeException e) {
        return ResponseEntity
                .status(INVALID_LETTER_TYPE.getHttpStatus())
                .header("Content-Type", "application/json")
                .body(ApiResponse.onFailure(INVALID_LETTER_TYPE.getCode(), e.getMessage(), null));
    }

    @ExceptionHandler(TempRecommendationsNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidLetterTypeException(
            TempRecommendationsNotFoundException e) {
        log.error("Temp recommendations not found: {}", e.getMessage());
        return ResponseEntity
                .status(TEMP_RECOMMENDATIONS_NOT_FOUND.getHttpStatus())
                .header("Content-Type", "application/json")
                .body(ApiResponse.onFailure(TEMP_RECOMMENDATIONS_NOT_FOUND.getCode(), e.getMessage(), null));
    }

    @ExceptionHandler(LetterAuthorMismatchException.class)
    public ResponseEntity<ApiResponse<String>> handleLetterAuthorMismatchException(LetterAuthorMismatchException e) {
        return ResponseEntity
                .status(LETTER_AUTHOR_MISMATCH.getHttpStatus())
                .header("Content-Type", "application/json")
                .body(ApiResponse.onFailure(LETTER_AUTHOR_MISMATCH.getCode(), e.getMessage(), null));
    }

    @ExceptionHandler(UnauthorizedLetterAccessException.class)
    public ResponseEntity<ApiResponse<String>> handleUnauthorizedLetterAccessException(
            UnauthorizedLetterAccessException e) {
        return ResponseEntity
                .status(UNAUTHORIZED_LETTER_ACCESS.getHttpStatus())
                .header("Content-Type", "application/json")
                .body(ApiResponse.onFailure(UNAUTHORIZED_LETTER_ACCESS.getCode(), e.getMessage(), null));
    }

    @ExceptionHandler(DeveloperLetterException.class)
    public ApiResponse<?> handleDeveloperLetterException(DeveloperLetterException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.DEVELOPER_LETTER.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidInputException(
            InvalidInputException e
    ) {
        log.error("Invalid input: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorStatus.INVALID_INPUT.getHttpStatus())
                .header("Content-Type", "application/json")
                .body(ApiResponse.onFailure(ErrorStatus.INVALID_INPUT.getCode(), e.getMessage(), null));
    }
}
