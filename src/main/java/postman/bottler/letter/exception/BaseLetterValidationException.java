package postman.bottler.letter.exception;

import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class BaseLetterValidationException extends RuntimeException {
    private final Map<String, String> errors;

    public BaseLetterValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
}
