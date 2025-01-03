package postman.bottler.mapletter.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
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
        LocalDateTime createdAt
) {
    public static FindAllReceivedLetterResponseDTO from(MapLetter mapLetter, String sendUserNickname,
                                                        String sendUserProfileImg) {
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
                .build();
    }
}
