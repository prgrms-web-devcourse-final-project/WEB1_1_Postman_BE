package postman.bottler.letter.exception;

import java.util.Map;

public class InvalidLetterRequestException extends BaseLetterValidationException {
    public InvalidLetterRequestException(String message, Map<String, String> errors) {
        super(message, errors);
    }
}
