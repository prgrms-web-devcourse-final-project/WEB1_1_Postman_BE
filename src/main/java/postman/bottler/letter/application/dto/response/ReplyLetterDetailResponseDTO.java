package postman.bottler.letter.application.dto.response;

import java.time.LocalDateTime;
import postman.bottler.letter.domain.ReplyLetter;

public record ReplyLetterDetailResponseDTO(
        Long replyLetterId,
        String content,
        String font,
        String paper,
        String label,
        boolean isReplied,
        LocalDateTime createdAt
) {
    public static ReplyLetterDetailResponseDTO from(ReplyLetter replyLetter, boolean isReplied) {
        return new ReplyLetterDetailResponseDTO(
                replyLetter.getId(),
                replyLetter.getContent(),
                replyLetter.getFont(),
                replyLetter.getPaper(),
                replyLetter.getLabel(),
                isReplied,
                replyLetter.getCreatedAt()
        );
    }
}
