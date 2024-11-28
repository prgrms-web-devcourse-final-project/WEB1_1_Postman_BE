package postman.bottler.complaint.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComplaintRequestDTO(
        @NotNull(message = "편지의 작성자는 필수입니다.")
        Long writerId,
        @NotBlank(message = "신고 사유를 작성해주세요.")
        String description,
        // TODO JWT 구현 시, reporterId 삭제
        Long reporterId
) {
}
