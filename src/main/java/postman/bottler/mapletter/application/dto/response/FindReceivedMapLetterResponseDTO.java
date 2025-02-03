package postman.bottler.mapletter.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import postman.bottler.mapletter.application.dto.FindReceivedMapLetterDTO;
import postman.bottler.mapletter.application.dto.request.DeleteMapLettersRequestDTO;

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
        String senderProfileImg, //타겟편지에서 보낸 사람의 프로필 이미지
        DeleteMapLettersRequestDTO.LetterType deleteType, //삭제 타입
        boolean isRead //한 번이라도 읽었는지
) {
    public static FindReceivedMapLetterResponseDTO from(FindReceivedMapLetterDTO letterDTO, String senderNickname,
                                                        String senderProfileImg,
                                                        DeleteMapLettersRequestDTO.LetterType deleteType) {
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
                .deleteType(deleteType)
                .isRead(letterDTO.getIsRead() == 1)
                .build();
    }
}
