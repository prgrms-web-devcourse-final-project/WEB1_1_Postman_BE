package postman.bottler.letter.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import postman.bottler.keyword.domain.LetterKeyword;
import postman.bottler.letter.domain.Letter;

public record LetterDetailResponseDTO(
        Long letterId,
        String title,
        String content,
        List<String> keywords,
        String font,
        String paper,
        String profile,
        String label,
        boolean isOwner,
        LocalDateTime createdAt
) {
    public static LetterDetailResponseDTO from(Letter letter, List<LetterKeyword> letterKeywords, Long currentUserId,
                                               String profile) {
        return new LetterDetailResponseDTO(
                letter.getId(),
                letter.getTitle(),
                letter.getContent(),
                letterKeywords.stream().map(LetterKeyword::getKeyword).toList(),
                letter.getFont(),
                letter.getPaper(),
                profile,
                letter.getLabel(),
                letter.getUserId().equals(currentUserId),
                letter.getCreatedAt()
        );
    }
}
