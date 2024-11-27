package postman.bottler.mapletter.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OneLetterResponseDTO(
        String title,
        String content,
        String description,
        String profileImg,
        String font,
        String paper,
        String label,
        LocalDateTime createdAt
) {
}
