package postman.bottler.letter.exception;

import java.util.Map;

public class InvalidReplyLetterRequestException extends BaseLetterValidationException {
    public InvalidReplyLetterRequestException(String message, Map<String, String> errors) {
        super(message, errors);
    }
}
