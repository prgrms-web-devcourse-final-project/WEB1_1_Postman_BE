package postman.bottler.letter.exception;

import postman.bottler.global.response.code.ErrorStatus;

public class LetterAccessDeniedException extends LetterCustomException {
    public LetterAccessDeniedException() {
        super(ErrorStatus.LETTER_ACCESS_DENIED, "편지 접근이 제한되었습니다.");
    }
}
