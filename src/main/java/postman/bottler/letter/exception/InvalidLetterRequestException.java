package postman.bottler.letter.exception;

import static postman.bottler.global.response.code.ErrorStatus.INVALID_LETTER_REQUEST;

import lombok.Getter;

@Getter
public class InvalidLetterRequestException extends LetterCustomException {
    public InvalidLetterRequestException() {
        super(INVALID_LETTER_REQUEST, "잘못된 편지 요청입니다.");
    }

    public InvalidLetterRequestException(String message) {
        super(INVALID_LETTER_REQUEST, message);
    }
}
