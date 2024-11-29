package postman.bottler.letter.exception;

public class SavedLetterNotFoundException extends RuntimeException {

    public SavedLetterNotFoundException(String message) {
        super(message);
    }
}
