package postman.bottler.mapletter.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import postman.bottler.mapletter.domain.ReplyMapLetter;

@Builder
public record FindAllSentReplyMapLetterResponseDTO(
        Long letterId,
        String title,
        Long sourceLetterId,
        LocalDateTime createdAt,
        String label
) {
    public static FindAllSentReplyMapLetterResponseDTO from(ReplyMapLetter replyMapLetter, String title) {
        return FindAllSentReplyMapLetterResponseDTO.builder()
                .letterId(replyMapLetter.getReplyLetterId())
                .title(title)
                .sourceLetterId(replyMapLetter.getSourceLetterId())
                .createdAt(replyMapLetter.getCreatedAt())
                .label(replyMapLetter.getLabel())
                .build();
    }
}
