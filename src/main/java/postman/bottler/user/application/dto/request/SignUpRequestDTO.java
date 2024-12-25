package postman.bottler.user.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpRequestDTO(
        @NotBlank(message = "이메일은 필수 입력입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력입니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).{8,25}$", message = "유효한 비밀번호 형식이 아닙니다.")
        String password,

        @NotBlank(message = "닉네임은 필수 입력입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣]{1,8}$", message = "유효한 닉네임 형식이 아닙니다.")
        String nickname
) {
}