package postman.bottler.letter.exception;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;

@RestControllerAdvice
@Slf4j
public class LetterExceptionHandler {
    @ExceptionHandler(LetterCustomException.class)
    public ResponseEntity<ApiResponse<?>> handleLetterException(LetterCustomException e) {
        return buildErrorResponse(e);
    }

    @ExceptionHandler(LetterValidationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(LetterValidationException e) {
        log.error("유효성 검사 실패 - 상태 코드: {}, 메시지: {}, 상세 오류: {}", e.getErrorStatus(), e.getMessage(),
                formatErrors(e.getErrors()));

        return ResponseEntity.status(e.getErrorStatus().getHttpStatus())
                .body(ApiResponse.onFailure(e.getErrorStatus().getCode(), e.getMessage(), e.getErrors()));
    }

    private ResponseEntity<ApiResponse<?>> buildErrorResponse(LetterCustomException e) {
        log.error("{}: {}", e.getErrorStatus(), e.getMessage());
        return ResponseEntity.status(e.getErrorStatus().getHttpStatus())
                .body(ApiResponse.onFailure(e.getErrorStatus().getCode(), e.getMessage(), null));
    }

    private String formatErrors(Map<String, String> errors) {
        if (errors == null || errors.isEmpty()) {
            return "세부 오류 없음";
        }
        return errors.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue())
                .reduce((msg1, msg2) -> msg1 + ", " + msg2).orElse("세부 오류 없음");
    }
}
