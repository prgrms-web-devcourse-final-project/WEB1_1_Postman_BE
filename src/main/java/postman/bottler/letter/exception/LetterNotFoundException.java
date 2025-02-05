package postman.bottler.letter.exception;

import static postman.bottler.global.response.code.ErrorStatus.LETTER_NOT_FOUND;

import postman.bottler.letter.domain.LetterType;

public class LetterNotFoundException extends LetterCustomException {

    public LetterNotFoundException() {
        super(LETTER_NOT_FOUND, "편지가 존재하지 않습니다.");
    }

    public LetterNotFoundException(LetterType letterType) {
        super(LETTER_NOT_FOUND, getMessageByLetterType(letterType));
    }

    private static String getMessageByLetterType(LetterType letterType) {
        return switch (letterType) {
            case LETTER -> "키워드 편지가 존재하지 않습니다.";
            case REPLY_LETTER -> "키워드 답장 편지가 존재하지 않습니다.";
            case UNKNOWN -> "편지가 존재하지 않습니다.";
        };
    }
}
