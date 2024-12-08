package postman.bottler.letter.exception;

public class UnauthorizedLetterAccessException extends RuntimeException {
    public UnauthorizedLetterAccessException(String message) {
        super(message);
    }
}
