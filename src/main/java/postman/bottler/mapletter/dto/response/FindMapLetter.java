package postman.bottler.mapletter.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindMapLetter(
        Long letterId,
        String title,
        String description,
        String label,
        String targetUserNickname,
        LocalDateTime createdAt
) {
}
