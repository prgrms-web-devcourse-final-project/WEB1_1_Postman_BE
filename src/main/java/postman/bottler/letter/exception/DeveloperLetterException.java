package postman.bottler.letter.exception;

import postman.bottler.global.response.code.ErrorStatus;

public class DeveloperLetterException extends LetterCustomException {
    public DeveloperLetterException() {
        super(ErrorStatus.DEVELOPER_LETTER, "개발자 편지 에러입니다.");
    }
}
