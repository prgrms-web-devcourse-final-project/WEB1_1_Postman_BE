package postman.bottler.mapletter.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FindAllArchiveLetters(
        Long archiveId,
        Long letterId,
        String title,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        String label,
        LocalDateTime createdAt,
        LocalDateTime letterCreatedAt
) {
}
