package postman.bottler.letter.exception;

import postman.bottler.global.response.code.ErrorStatus;

public class InvalidLetterTypeException extends LetterCustomException {
    public InvalidLetterTypeException() {
        super(ErrorStatus.INVALID_LETTER_TYPE, "유효하지 않은 편지 타입입니다.");
    }
}
