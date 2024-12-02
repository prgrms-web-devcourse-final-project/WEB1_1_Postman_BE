package postman.bottler.mapletter.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import postman.bottler.mapletter.domain.MapLetter;

@Builder
public record FindAllReceivedLetterResponseDTO(
        Long letterId,
        String title,
        String description,
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
                .label(mapLetter.getLabel())
                .sendUserNickname(sendUserNickname)
                .sendUserProfileImg(sendUserProfileImg)
                .createdAt(mapLetter.getCreatedAt())
                .build();
    }
}
