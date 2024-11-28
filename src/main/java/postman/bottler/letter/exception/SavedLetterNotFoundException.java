package postman.bottler.letter.exception;

import lombok.Getter;

@Getter
public class SavedLetterNotFoundException extends RuntimeException {

    public SavedLetterNotFoundException(String message) {
        super(message);
    }
}
