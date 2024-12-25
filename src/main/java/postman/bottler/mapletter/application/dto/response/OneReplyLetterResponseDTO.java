package postman.bottler.mapletter.application.dto.response;

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
        LocalDateTime createdAt,
        boolean isOwner
) {
    public static OneReplyLetterResponseDTO from(ReplyMapLetter replyMapLetter, boolean isOwner) {
        return OneReplyLetterResponseDTO.builder()
                .content(replyMapLetter.getContent())
                .sourceLetterId(replyMapLetter.getSourceLetterId())
                .font(replyMapLetter.getFont())
                .label(replyMapLetter.getLabel())
                .paper(replyMapLetter.getPaper())
                .createdAt(replyMapLetter.getCreatedAt())
                .isOwner(isOwner)
                .build();
    }
}
