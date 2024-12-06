package postman.bottler.letter.exception;

public class DuplicateReplyLetterException extends RuntimeException {
    public DuplicateReplyLetterException(String message) {
        super(message);
    }
}
