package postman.bottler.letter.exception;

import lombok.Getter;

@Getter
public class LetterAlreadySavedException extends RuntimeException {

    public LetterAlreadySavedException(String message) {
        super(message);
    }
}
