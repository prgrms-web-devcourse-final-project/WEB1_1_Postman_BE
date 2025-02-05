package postman.bottler.letter.exception;

import postman.bottler.global.response.code.ErrorStatus;

public class LetterAlreadySavedException extends LetterCustomException {

    public LetterAlreadySavedException() {
        super(ErrorStatus.LETTER_ALREADY_SAVED, "편지가 이미 저장되었습니다.");
    }
}
