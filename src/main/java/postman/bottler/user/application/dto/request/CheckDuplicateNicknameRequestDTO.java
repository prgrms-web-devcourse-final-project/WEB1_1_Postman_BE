package postman.bottler.user.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CheckDuplicateNicknameRequestDTO(
        @NotBlank(message = "닉네임은 필수 입력입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣]{1,8}$", message = "유효한 닉네임 형식이 아닙니다.")
        String nickname
) {
}
