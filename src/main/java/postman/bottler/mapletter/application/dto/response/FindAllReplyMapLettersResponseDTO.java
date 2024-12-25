package postman.bottler.mapletter.application.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import postman.bottler.mapletter.domain.ReplyMapLetter;

@Builder
public record FindAllReplyMapLettersResponseDTO(
        Long replyLetterId,
        String label,
        LocalDateTime createdAt
) {
    public static FindAllReplyMapLettersResponseDTO from(ReplyMapLetter replyMapLetter) {
        return FindAllReplyMapLettersResponseDTO.builder()
                .replyLetterId(replyMapLetter.getReplyLetterId())
                .label(replyMapLetter.getLabel())
                .createdAt(replyMapLetter.getCreatedAt())
                .build();
    }
}
