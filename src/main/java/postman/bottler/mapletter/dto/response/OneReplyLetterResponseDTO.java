package postman.bottler.mapletter.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OneReplyLetterResponseDTO(
        String content,
        Long sourceLetterId,
        String font,
        String label,
        String paper,
        LocalDateTime createdAt
) {
}
