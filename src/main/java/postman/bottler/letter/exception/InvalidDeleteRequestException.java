package postman.bottler.letter.exception;

import java.util.Map;

public class InvalidDeleteRequestException extends BaseLetterValidationException {
    public InvalidDeleteRequestException(String message, Map<String, String> errors) {
        super(message, errors);
    }
}
