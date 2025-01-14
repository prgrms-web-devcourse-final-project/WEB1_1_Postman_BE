package postman.bottler.mapletter.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import postman.bottler.mapletter.application.dto.request.DeleteMapLettersRequestDTO;
import postman.bottler.mapletter.domain.MapLetter;

@Builder
public record FindAllReceivedLetterResponseDTO(
        Long letterId,
        String title,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        String label,
        String sendUserNickname,
        String sendUserProfileImg,
        LocalDateTime createdAt,
        DeleteMapLettersRequestDTO.LetterType deleteType, //삭제 타입
        boolean isRead //한 번이라도 읽었는지
) {
    public static FindAllReceivedLetterResponseDTO from(MapLetter mapLetter, String sendUserNickname,
                                                        String sendUserProfileImg,
                                                        DeleteMapLettersRequestDTO.LetterType deleteType) {
        return FindAllReceivedLetterResponseDTO.builder()
                .letterId(mapLetter.getId())
                .title(mapLetter.getTitle())
                .description(mapLetter.getDescription())
                .latitude(mapLetter.getLatitude())
                .longitude(mapLetter.getLongitude())
                .label(mapLetter.getLabel())
                .sendUserNickname(sendUserNickname)
                .sendUserProfileImg(sendUserProfileImg)
                .createdAt(mapLetter.getCreatedAt())
                .deleteType(deleteType)
                .isRead(mapLetter.isRead())
                .build();
    }
}
