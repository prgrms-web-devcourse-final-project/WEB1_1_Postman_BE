package postman.bottler.letter.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import postman.bottler.letter.domain.Letter;

public record LetterResponseDTO(
        Long letterId,
        String title,
        String content,
        List<String> keywords,
        String font,
        String paper,
        String profile,
        String label,
        LocalDateTime createdAt
) {
    public static LetterResponseDTO from(Letter letter) {
        return new LetterResponseDTO(
                letter.getId(),
                letter.getTitle(),
                letter.getContent(),
                letter.getKeywords(),
                letter.getFont(),
                letter.getPaper(),
                letter.getLabel(),
                letter.getProfile(),
                letter.getCreatedAt()
        );
    }
}