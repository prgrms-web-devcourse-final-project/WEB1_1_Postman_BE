package postman.bottler.letter.exception;

import lombok.Getter;
import postman.bottler.global.response.code.ErrorStatus;

@Getter
public class LetterNotFoundException extends RuntimeException {

    private final ErrorStatus errorStatus;

    public LetterNotFoundException(String message) {
        super(message);
        this.errorStatus = ErrorStatus.LETTER_NOT_FOUND;
    }
}
