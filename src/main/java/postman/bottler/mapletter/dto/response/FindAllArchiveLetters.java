package postman.bottler.mapletter.dto.response;

import java.time.LocalDateTime;

public record FindAllArchiveLetters(
        Long archiveId,
        Long letterId,
        String title,
        String description,
        String label,
        LocalDateTime createdAt
) {
}
