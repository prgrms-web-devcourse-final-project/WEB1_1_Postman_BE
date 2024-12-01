package postman.bottler.letter.dto.response;

import java.time.LocalDateTime;
import postman.bottler.letter.domain.Letter;

public record LetterHeadersResponseDTO(
        Long letterId,
        String title,
        String label,
        LocalDateTime createdAt
) {
    public static LetterHeadersResponseDTO from(Letter letter) {
        return new LetterHeadersResponseDTO(
                letter.getId(),
                letter.getTitle(),
                letter.getLabel(),
                letter.getCreatedAt()
        );
    }
}
