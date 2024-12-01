package postman.bottler.mapletter.dto.response;

import java.math.BigDecimal;
import lombok.Builder;

import java.time.LocalDateTime;
import postman.bottler.mapletter.domain.MapLetter;

@Builder
public record OneLetterResponseDTO(
        Long letterId,
        String title,
        String content,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        String profileImg,
        String font,
        String paper,
        String label,
        LocalDateTime createdAt
) {
    public static OneLetterResponseDTO from(MapLetter mapLetter, String profileImg) {
        return OneLetterResponseDTO.builder()
                .letterId(mapLetter.getId())
                .title(mapLetter.getTitle())
                .content(mapLetter.getContent())
                .font(mapLetter.getFont())
                .paper(mapLetter.getPaper())
                .label(mapLetter.getLabel())
                .profileImg(profileImg)
                .createdAt(mapLetter.getCreatedAt())
                .description(mapLetter.getDescription())
                .latitude(mapLetter.getLatitude())
                .longitude(mapLetter.getLongitude())
                .build();
    }
}
