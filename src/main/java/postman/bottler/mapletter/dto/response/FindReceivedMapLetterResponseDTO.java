package postman.bottler.mapletter.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindReceivedMapLetterResponseDTO(
        Long letterId,
        String title,
        String description,
        String label,
        LocalDateTime createdAt,
        String type, //reply, target
        Long sourceLetterId,
        String senderNickname //타겟편지에서 누가 나한테 보냈는지
) {
}
