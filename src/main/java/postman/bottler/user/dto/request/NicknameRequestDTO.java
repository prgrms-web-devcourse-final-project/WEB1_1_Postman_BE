package postman.bottler.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NicknameRequestDTO(
        @NotBlank(message = "닉네임은 필수 입력입니다.")
        String nickname
) {
}
