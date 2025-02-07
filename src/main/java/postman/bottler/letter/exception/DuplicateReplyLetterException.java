package postman.bottler.letter.exception;

import postman.bottler.global.response.code.ErrorStatus;

public class DuplicateReplyLetterException extends LetterCustomException {
    public DuplicateReplyLetterException(Long letterId, Long senderId) {
        super(ErrorStatus.DUPLICATE_REPLY_LETTER,
                String.format("이미 이 편지에 답장한 기록이 있습니다. letterId=%d, senderId=%d", letterId, senderId));
    }
}
