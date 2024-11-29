package postman.bottler.letter.exception;

public class LetterAccessDeniedException extends RuntimeException {
    public LetterAccessDeniedException(String message) {
        super(message);
    }
}
