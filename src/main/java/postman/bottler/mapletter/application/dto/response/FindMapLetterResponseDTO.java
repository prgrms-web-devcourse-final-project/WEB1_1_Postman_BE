package postman.bottler.mapletter.application.dto.response;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;
import org.hibernate.sql.Delete;
import postman.bottler.mapletter.application.dto.FindReceivedMapLetterDTO;
import postman.bottler.mapletter.application.dto.FindSentMapLetter;
import postman.bottler.mapletter.application.dto.request.DeleteMapLettersRequestDTO;
import postman.bottler.mapletter.application.dto.request.DeleteMapLettersRequestDTO.LetterType;

@Builder
public record FindMapLetterResponseDTO(
        Long letterId,
        String title, // 답장 편지의 경우 Re: 답장 편지 이름
        String description, // 답장 편지의 경우 생략
        String label,
        String targetUserNickname, // 타겟 편지의 경우만
        LocalDateTime createdAt,
        String type, //REPLY(답장 편지), TARGET(타겟 편지), PUBLIC(퍼블릭 편지)
        Long sourceLetterId, //답장 편지의 경우 원본 편지 id
        DeleteMapLettersRequestDTO.LetterType deleteType //삭제 타입
) {
    public static FindMapLetterResponseDTO from(FindSentMapLetter projection, String targetUserNickname,
                                                LetterType deleteType) {
        return FindMapLetterResponseDTO.builder()
                .letterId(projection.getLetterId())
                .title(projection.getTitle())
                .description(projection.getDescription())
                .label(projection.getLabel())
                .targetUserNickname(targetUserNickname)
                .createdAt(projection.getCreatedAt())
                .type(projection.getType())
                .sourceLetterId(projection.getSourceLetterId())
                .deleteType(deleteType)
                .build();
    }
}
