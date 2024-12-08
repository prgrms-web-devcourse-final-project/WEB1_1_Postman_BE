package postman.bottler.letter.dto;

import java.time.LocalDateTime;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterBox;
import postman.bottler.letter.domain.LetterType;

public record LetterBoxDTO(
        Long userId,
        Long letterId,
        LetterType letterType,
        BoxType boxType,
        LocalDateTime createdAt
) {
    public static LetterBoxDTO of(Long userId, Long letterId, LetterType letterType, BoxType boxType,
                                  LocalDateTime createdAt) {
        return new LetterBoxDTO(userId, letterId, letterType, boxType, createdAt);
    }

    public LetterBox toDomain() {
        return LetterBox.builder()
                .userId(userId)
                .letterId(letterId)
                .letterType(letterType)
                .boxType(boxType)
                .createdAt(createdAt)
                .build();
    }
}
