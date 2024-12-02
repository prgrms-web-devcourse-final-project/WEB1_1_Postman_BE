package postman.bottler.letter.dto.request;

import jakarta.validation.constraints.NotNull;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;

public record LetterDeleteRequestDTO(
        @NotNull(message = "Letter ID는 필수입니다.") Long letterId,
        @NotNull(message = "Letter Type은 필수입니다.") LetterType letterType,
        @NotNull(message = "Box Type은 필수입니다.") BoxType boxType
) {
    public static LetterDeleteRequestDTO of(Long letterId, LetterType letterType, BoxType boxType) {
        return new LetterDeleteRequestDTO(
                letterId,
                letterType,
                boxType
        );
    }
}
