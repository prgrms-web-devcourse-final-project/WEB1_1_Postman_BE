package postman.bottler.mapletter.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import postman.bottler.mapletter.domain.ReplyMapLetter;

@Builder
public record FindAllReceivedReplyLetterResponseDTO(
        Long letterId,
        String title,
        String label,
        LocalDateTime createdAt,
        Long sourceLetterId
) {
    public static FindAllReceivedReplyLetterResponseDTO from(ReplyMapLetter letterDTO, String title) {
        return FindAllReceivedReplyLetterResponseDTO.builder()
                .letterId(letterDTO.getReplyLetterId())
                .title(title)
                .label(letterDTO.getLabel())
                .createdAt(letterDTO.getCreatedAt())
                .sourceLetterId(letterDTO.getSourceLetterId())
                .build();
    }
}
