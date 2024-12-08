package postman.bottler.letter.exception;

public class InvalidLetterTypeException extends RuntimeException {
    public InvalidLetterTypeException(String message) {
        super(message);
    }
}
