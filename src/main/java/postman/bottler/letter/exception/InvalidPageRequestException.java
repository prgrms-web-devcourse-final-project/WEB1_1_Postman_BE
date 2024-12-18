package postman.bottler.letter.exception;

import java.util.Map;

public class InvalidPageRequestException extends BaseLetterValidationException {
    public InvalidPageRequestException(String message, Map<String, String> errors) {
        super(message, errors);
    }
}
