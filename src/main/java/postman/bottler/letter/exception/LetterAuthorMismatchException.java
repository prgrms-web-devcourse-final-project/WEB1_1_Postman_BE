package postman.bottler.letter.exception;

import postman.bottler.global.response.code.ErrorStatus;

public class LetterAuthorMismatchException extends LetterCustomException {
    public LetterAuthorMismatchException() {
        super(ErrorStatus.LETTER_AUTHOR_MISMATCH, "요청자와 작성자가 일치하지 않습니다.");
    }
}
