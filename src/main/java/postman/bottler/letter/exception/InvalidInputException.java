package postman.bottler.letter.exception;

import postman.bottler.global.response.code.ErrorStatus;

public class InvalidInputException extends LetterCustomException {
    public InvalidInputException() {
        super(ErrorStatus.INVALID_INPUT, "유효하지 않은 userId");
    }
}
