package postman.bottler.mapletter.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import postman.bottler.mapletter.application.dto.request.DeleteMapLettersRequestDTO;
import postman.bottler.mapletter.domain.ReplyMapLetter;

@Builder
public record FindAllSentReplyMapLetterResponseDTO(
        Long letterId,
        String title,
        Long sourceLetterId,
        LocalDateTime createdAt,
        String label,
        DeleteMapLettersRequestDTO.LetterType deleteType //삭제 타입
) {
    public static FindAllSentReplyMapLetterResponseDTO from(ReplyMapLetter replyMapLetter, String title,
                                                            DeleteMapLettersRequestDTO.LetterType deleteType) {
        return FindAllSentReplyMapLetterResponseDTO.builder()
                .letterId(replyMapLetter.getReplyLetterId())
                .title(title)
                .sourceLetterId(replyMapLetter.getSourceLetterId())
                .createdAt(replyMapLetter.getCreatedAt())
                .label(replyMapLetter.getLabel())
                .deleteType(deleteType)
                .build();
    }
}
