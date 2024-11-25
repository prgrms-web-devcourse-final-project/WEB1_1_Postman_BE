package postman.bottler.mapletter.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OneLetterResponse(
        String title,
        String content,
        String profileImg,
        String font,
        String paper,
        String label,
        LocalDateTime createdAt
) {
}
