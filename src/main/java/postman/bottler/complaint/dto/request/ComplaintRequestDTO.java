package postman.bottler.complaint.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ComplaintRequestDTO(
        @NotBlank(message = "신고 사유를 작성해주세요.")
        String description,
        // TODO JWT 구현 시, reporterId 삭제
        Long reporterId
) {
}
