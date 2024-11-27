package postman.bottler.letter.dto.response;

import java.time.LocalDateTime;
import postman.bottler.letter.domain.ReplyLetter;

public record ReplyLetterHeadersResponseDTO(
        Long replyLetterId,
        String title,
        String label,
        LocalDateTime createdAt
) {
    public static ReplyLetterHeadersResponseDTO from(ReplyLetter replyLetter) {
        return new ReplyLetterHeadersResponseDTO(
                replyLetter.getId(),
                replyLetter.getTitle(),
                replyLetter.getLabel(),
                replyLetter.getCreatedAt()
        );
    }
}
