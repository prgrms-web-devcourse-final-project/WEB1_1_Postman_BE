package postman.bottler.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record KakaoRequestDTO(
        @NotBlank(message = "인가코드는 필수 입력입니다.")
        String code
) {
}
