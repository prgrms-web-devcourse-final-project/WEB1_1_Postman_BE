package postman.bottler.complaint.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ComplaintRequestDTO(
        @NotBlank(message = "신고 사유를 작성해주세요.")
        String description
) {
}
