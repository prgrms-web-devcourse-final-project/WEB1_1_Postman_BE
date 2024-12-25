package postman.bottler.complaint.application.dto.response;

import postman.bottler.complaint.domain.Complaint;

public record ComplaintResponseDTO(
        Long id,
        String description
) {
    public static ComplaintResponseDTO from(Complaint complaint) {
        return new ComplaintResponseDTO(complaint.getId(), complaint.getDescription());
    }
}
