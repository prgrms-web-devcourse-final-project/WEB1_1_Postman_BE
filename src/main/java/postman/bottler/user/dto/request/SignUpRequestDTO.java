package postman.bottler.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequestDTO(
        @NotBlank(message = "이메일은 필수 입력입니다.")
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력입니다.")
//        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상, 20자 이하이어야 합니다.") // TODO: 정책 정해야 함
        String password,

        @NotBlank(message = "닉네임은 필수 입력입니다.")
//        @Size(min = 3, max = 15, message = "닉네임은 3자 이상, 15자 이하이어야 합니다.") // TODO: 정책 정해야 함
        String nickname
) {
}