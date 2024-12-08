package postman.bottler.letter.dto;

import jakarta.validation.constraints.NotNull;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.dto.request.LetterDeleteRequestDTO;
import postman.bottler.letter.dto.request.ReplyLetterDeleteRequestDTO;

public record LetterDeleteDTO(
        @NotNull(message = "Letter ID는 필수입니다.") Long letterId,
        @NotNull(message = "Letter Type은 필수입니다.") LetterType letterType,
        @NotNull(message = "Box Type은 필수입니다.") BoxType boxType
) {
    public static LetterDeleteDTO fromLetter(LetterDeleteRequestDTO letterDeleteRequestDTO) {
        return new LetterDeleteDTO(
                letterDeleteRequestDTO.letterId(),
                LetterType.LETTER,
                letterDeleteRequestDTO.boxType()
        );
    }

    public static LetterDeleteDTO fromReplyLetter(ReplyLetterDeleteRequestDTO replyLetterDeleteRequestDTO) {
        return new LetterDeleteDTO(
                replyLetterDeleteRequestDTO.letterId(),
                LetterType.REPLY_LETTER,
                replyLetterDeleteRequestDTO.boxType()
        );
    }
}
