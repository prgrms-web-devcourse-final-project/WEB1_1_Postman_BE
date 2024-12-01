package postman.bottler.mapletter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReplyMapLetterRequestDTO(
        @NotNull(message = "원본 편지는 생략이 불가능합니다.") Long sourceLetter,
        @NotBlank(message = "편지 내용은 생략이 불가능합니다.") String content,
        String font,
        String paper,
        String label
) {
}
