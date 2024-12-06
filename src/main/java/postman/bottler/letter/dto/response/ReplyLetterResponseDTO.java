package postman.bottler.letter.dto.response;

import java.time.LocalDateTime;
import postman.bottler.letter.domain.ReplyLetter;

public record ReplyLetterResponseDTO(
        Long replyLetterId,
        String content,
        String font,
        String paper,
        String label,
        LocalDateTime createdAt
) {
    public static ReplyLetterResponseDTO from(ReplyLetter replyLetter) {
        return new ReplyLetterResponseDTO(
                replyLetter.getId(),
                replyLetter.getContent(),
                replyLetter.getFont(),
                replyLetter.getPaper(),
                replyLetter.getLabel(),
                replyLetter.getCreatedAt()
        );
    }
}
