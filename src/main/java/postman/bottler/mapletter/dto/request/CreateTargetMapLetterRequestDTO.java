package postman.bottler.mapletter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateTargetMapLetterRequestDTO(
        @NotBlank(message = "편지 제목은 생략이 불가능합니다.") String title,
        @NotBlank(message = "편지 내용은 생략이 불가능합니다.") String content,
        BigDecimal latitude,
        BigDecimal longitude,
        String font,
        int paper,
        int label,
        @NotNull(message="타겟 유저는 생략할 수 없습니다.") Long target
) {
}
