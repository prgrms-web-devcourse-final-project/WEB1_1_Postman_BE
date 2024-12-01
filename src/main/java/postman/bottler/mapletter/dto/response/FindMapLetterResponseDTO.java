package postman.bottler.mapletter.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import postman.bottler.mapletter.dto.FindSentMapLetter;

@Builder
public record FindMapLetterResponseDTO(
        Long letterId,
        String title, // 답장 편지의 경우 Re: 답장 편지 이름
        String description, // 답장 편지의 경우 생략
        String label,
        String targetUserNickname, // 타겟 편지의 경우만
        LocalDateTime createdAt,
        String type //REPLY(답장 편지), TARGET(타겟 편지), PUBLIC(퍼블릭 편지)
) {
    public static FindMapLetterResponseDTO from(FindSentMapLetter projection, String targetUserNickname) {
        return FindMapLetterResponseDTO.builder()
                .letterId(projection.getLetterId())
                .title(projection.getTitle())
                .description(projection.getDescription())
                .label(projection.getLabel())
                .targetUserNickname(targetUserNickname)
                .createdAt(projection.getCreatedAt())
                .type(projection.getType())
                .build();
    }
}
