package postman.bottler.letter.dto.response;

import java.time.LocalDateTime;
import java.util.List;
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
    public static LetterDetailResponseDTO from(Letter letter, List<String> keywords, Long userId, String profile) {
        return new LetterDetailResponseDTO(
                letter.getId(),
                letter.getTitle(),
                letter.getContent(),
                keywords,
                letter.getFont(),
                letter.getPaper(),
                profile,
                letter.getLabel(),
                letter.getUserId().equals(userId),
                letter.getCreatedAt()
        );
    }
}
