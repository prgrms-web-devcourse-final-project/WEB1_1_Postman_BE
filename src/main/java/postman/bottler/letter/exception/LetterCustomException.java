package postman.bottler.letter.exception;

import lombok.Getter;
import postman.bottler.global.response.code.ErrorStatus;

@Getter
public class LetterCustomException extends RuntimeException {
    private final ErrorStatus errorStatus;

    public LetterCustomException(ErrorStatus errorStatus, String message) {
        super(message);
        this.errorStatus = errorStatus;
    }
}
