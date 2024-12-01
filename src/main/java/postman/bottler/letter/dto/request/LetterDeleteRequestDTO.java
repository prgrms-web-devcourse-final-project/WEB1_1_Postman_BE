package postman.bottler.letter.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LetterDeleteRequestDTO(
        @NotBlank(message = "Letter ID는 필수입니다.") Long letterId,
        @NotBlank(message = "Letter Type은 필수입니다.") String letterType,
        @NotBlank(message = "Box Type은 필수입니다.") String boxType
) {
}
