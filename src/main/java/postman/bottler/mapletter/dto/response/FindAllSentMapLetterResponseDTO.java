package postman.bottler.mapletter.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.domain.MapLetterType;

@Builder
public record FindAllSentMapLetterResponseDTO(
        Long letterId,
        String title,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        String label,
        String targetUserNickname,
        LocalDateTime createdAt,
        String type //TARGET(타겟 편지), PUBLIC(퍼블릭 편지)
) {
    public static FindAllSentMapLetterResponseDTO from(MapLetter mapLetter, String targetUserNickname) {
        return FindAllSentMapLetterResponseDTO.builder()
                .letterId(mapLetter.getId())
                .title(mapLetter.getTitle())
                .description(mapLetter.getDescription())
                .latitude(mapLetter.getLatitude())
                .longitude(mapLetter.getLongitude())
                .label(mapLetter.getLabel())
                .targetUserNickname(targetUserNickname)
                .createdAt(mapLetter.getCreatedAt())
                .type(mapLetter.getType() == MapLetterType.PRIVATE ? "TARGET" : "PUBLIC")
                .build();
    }
}
