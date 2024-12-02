package postman.bottler.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CheckPasswordDTO(
        @NotBlank(message = "비밀번호는 필수 입력입니다.")
        String password
) {
}
