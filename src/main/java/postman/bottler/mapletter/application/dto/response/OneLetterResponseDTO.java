package postman.bottler.mapletter.application.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import postman.bottler.mapletter.domain.MapLetter;

@Builder
public record OneLetterResponseDTO(
        String title,
        String content,
        String description,
        String profileImg,
        String font,
        String paper,
        String label,
        LocalDateTime createdAt,
        boolean isOwner
) {
    public static OneLetterResponseDTO from(MapLetter mapLetter, String profileImg, boolean isOwner) {
        return OneLetterResponseDTO.builder()
                .title(mapLetter.getTitle())
                .content(mapLetter.getContent())
                .font(mapLetter.getFont())
                .paper(mapLetter.getPaper())
                .label(mapLetter.getLabel())
                .profileImg(profileImg)
                .createdAt(mapLetter.getCreatedAt())
                .description(mapLetter.getDescription())
                .isOwner(isOwner)
                .build();
    }
}