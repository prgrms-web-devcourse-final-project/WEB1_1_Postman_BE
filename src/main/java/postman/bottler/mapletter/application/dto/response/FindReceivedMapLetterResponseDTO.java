package postman.bottler.mapletter.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import postman.bottler.mapletter.application.dto.FindReceivedMapLetterDTO;

@Builder
public record FindReceivedMapLetterResponseDTO(
        Long letterId,
        String title,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        String label,
        LocalDateTime createdAt,
        String type, //reply, target
        Long sourceLetterId,
        String senderNickname, //타겟편지에서 누가 나한테 보냈는지
        String senderProfileImg //타겟편지에서 보낸 사람의 프로필 이미지
) {
    public static FindReceivedMapLetterResponseDTO from(FindReceivedMapLetterDTO letterDTO, String senderNickname,
                                                        String senderProfileImg) {
        return FindReceivedMapLetterResponseDTO.builder()
                .letterId(letterDTO.getLetterId())
                .title(letterDTO.getTitle())
                .description(letterDTO.getDescription())
                .latitude(letterDTO.getLatitude())
                .longitude(letterDTO.getLongitude())
                .label(letterDTO.getLabel())
                .createdAt(letterDTO.getCreatedAt())
                .type(letterDTO.getType())
                .sourceLetterId(letterDTO.getSourceLetterId())
                .senderNickname(senderNickname)
                .senderProfileImg(senderProfileImg)
                .build();
    }
}
