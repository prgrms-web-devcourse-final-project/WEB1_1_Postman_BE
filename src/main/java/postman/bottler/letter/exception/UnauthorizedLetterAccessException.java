package postman.bottler.letter.exception;

import postman.bottler.global.response.code.ErrorStatus;

public class UnauthorizedLetterAccessException extends LetterCustomException {
    public UnauthorizedLetterAccessException() {
        super(ErrorStatus.UNAUTHORIZED_LETTER_ACCESS, "해당 편지에 접근할 권한이 없습니다.");
    }
}
