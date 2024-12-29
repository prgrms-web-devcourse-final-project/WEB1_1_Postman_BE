package postman.bottler.user.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ProfileImgRequestDTO(
        @NotBlank(message = "이미지 URL은 필수 입력입니다.")
        String imageUrl
) {
}
