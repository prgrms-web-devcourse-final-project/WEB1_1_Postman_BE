package postman.bottler.letter.exception;

public class LetterAlreadySavedException extends RuntimeException {

    public LetterAlreadySavedException(String message) {
        super(message);
    }
}
