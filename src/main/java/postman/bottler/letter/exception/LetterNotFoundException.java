package postman.bottler.letter.exception;

import lombok.Getter;

@Getter
public class LetterNotFoundException extends RuntimeException {

    public LetterNotFoundException(String message) {
        super(message);
    }
}
