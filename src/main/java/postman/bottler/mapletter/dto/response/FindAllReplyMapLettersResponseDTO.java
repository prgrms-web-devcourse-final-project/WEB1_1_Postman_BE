package postman.bottler.mapletter.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindAllReplyMapLettersResponseDTO(
        Long replyLetterId,
        String label,
        LocalDateTime createdAt
) {
}