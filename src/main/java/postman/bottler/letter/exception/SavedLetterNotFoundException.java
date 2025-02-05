package postman.bottler.letter.exception;

import postman.bottler.global.response.code.ErrorStatus;

public class SavedLetterNotFoundException extends LetterCustomException {

    public SavedLetterNotFoundException() {
        super(ErrorStatus.LETTER_NOT_FOUND, "저장된 편지를 찾을 수 없습니다.");
    }
}
