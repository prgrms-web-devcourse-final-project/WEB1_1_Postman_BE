package postman.bottler.letter.application.dto.response;

import java.time.LocalDateTime;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;

public record LetterSummaryResponseDTO(
        Long letterId,
        String title,
        String label,
        LetterType letterType,
        BoxType boxType,
        LocalDateTime createdAt
) {
}
