package postman.bottler.mapletter.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import postman.bottler.mapletter.domain.ReplyMapLetter;

@Builder
public record OneReplyLetterResponseDTO(
        String content,
        Long sourceLetterId,
        String font,
        String label,
        String paper,
        LocalDateTime createdAt
) {
    public static OneReplyLetterResponseDTO from(ReplyMapLetter replyMapLetter) {
        return OneReplyLetterResponseDTO.builder()
                .content(replyMapLetter.getContent())
                .sourceLetterId(replyMapLetter.getSourceLetterId())
                .font(replyMapLetter.getFont())
                .label(replyMapLetter.getLabel())
                .paper(replyMapLetter.getPaper())
                .createdAt(replyMapLetter.getCreatedAt())
                .build();
    }
}
