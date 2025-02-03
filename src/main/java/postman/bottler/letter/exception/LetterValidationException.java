package postman.bottler.letter.exception;

import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import postman.bottler.global.response.code.ErrorStatus;

@Slf4j
@Getter
public class LetterValidationException extends RuntimeException {
    private final Map<String, String> errors;
    private final ErrorStatus errorStatus;

    public LetterValidationException(String message, Map<String, String> errors, ErrorStatus errorStatus) {
        super(message);
        this.errors = errors;
        this.errorStatus = errorStatus;
    }
}
