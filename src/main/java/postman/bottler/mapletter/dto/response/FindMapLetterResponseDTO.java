package postman.bottler.mapletter.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import postman.bottler.mapletter.domain.MapLetter;

@Builder
public record FindMapLetterResponseDTO(
        Long letterId,
        String title,
        String description,
        String label,
        String targetUserNickname,
        LocalDateTime createdAt
) {
    public static FindMapLetterResponseDTO from(MapLetter mapLetter, String targetUserNickname) {
        return FindMapLetterResponseDTO.builder()
                .letterId(mapLetter.getId())
                .title(mapLetter.getTitle())
                .description(mapLetter.getDescription())
                .label(mapLetter.getLabel())
                .createdAt(mapLetter.getCreatedAt())
                .targetUserNickname(targetUserNickname)
                .build();
    }
}
