package postman.bottler.mapletter.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import postman.bottler.mapletter.application.dto.request.DeleteMapLettersRequestDTO;
import postman.bottler.mapletter.domain.ReplyMapLetter;

@Builder
public record FindAllReceivedReplyLetterResponseDTO(
        Long letterId,
        String title,
        String label,
        LocalDateTime createdAt,
        Long sourceLetterId,
        DeleteMapLettersRequestDTO.LetterType deleteType //삭제 타입
) {
    public static FindAllReceivedReplyLetterResponseDTO from(ReplyMapLetter letterDTO, String title,
                                                             DeleteMapLettersRequestDTO.LetterType deleteType) {
        return FindAllReceivedReplyLetterResponseDTO.builder()
                .letterId(letterDTO.getReplyLetterId())
                .title(title)
                .label(letterDTO.getLabel())
                .createdAt(letterDTO.getCreatedAt())
                .sourceLetterId(letterDTO.getSourceLetterId())
                .deleteType(deleteType)
                .build();
    }
}
