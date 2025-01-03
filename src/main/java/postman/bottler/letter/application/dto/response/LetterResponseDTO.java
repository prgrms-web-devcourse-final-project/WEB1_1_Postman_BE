package postman.bottler.letter.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import postman.bottler.keyword.domain.LetterKeyword;
import postman.bottler.letter.domain.Letter;

public record LetterResponseDTO(
        Long letterId,
        String title,
        String content,
        List<String> keywords,
        String font,
        String paper,
        String label,
        LocalDateTime createdAt
) {
    public static LetterResponseDTO from(Letter letter, List<LetterKeyword> letterKeywords) {
        return new LetterResponseDTO(
                letter.getId(),
                letter.getTitle(),
                letter.getContent(),
                letterKeywords.stream().map(LetterKeyword::getKeyword).toList(),
                letter.getFont(),
                letter.getPaper(),
                letter.getLabel(),
                letter.getCreatedAt()
        );
    }
}
