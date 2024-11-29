package postman.bottler.mapletter.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.domain.ReplyMapLetter;

@Builder
public record FindReceivedMapLetterResponseDTO(
        Long letterId,
        String title,
        String description,
        String label,
        LocalDateTime createdAt,
        String type, //reply, target
        Long sourceLetterId,
        String senderNickname //타겟편지에서 누가 나한테 보냈는지
) {
    public static FindReceivedMapLetterResponseDTO fromTargetMapLetter(MapLetter mapLetter, String senderNickname) {
        return FindReceivedMapLetterResponseDTO.builder()
                .letterId(mapLetter.getId())
                .title(mapLetter.getTitle())
                .description(mapLetter.getDescription())
                .label(mapLetter.getLabel())
                .createdAt(mapLetter.getCreatedAt())
                .type("target")
                .senderNickname(senderNickname)
                .build();
    }

    public static FindReceivedMapLetterResponseDTO fromReplyMapLetter(ReplyMapLetter mapLetter) {
        return FindReceivedMapLetterResponseDTO.builder()
                .letterId(mapLetter.getReplyLetterId())
                .label(mapLetter.getLabel())
                .createdAt(mapLetter.getCreatedAt())
                .type("reply")
                .sourceLetterId(mapLetter.getSourceLetterId())
                .build();
    }
}
