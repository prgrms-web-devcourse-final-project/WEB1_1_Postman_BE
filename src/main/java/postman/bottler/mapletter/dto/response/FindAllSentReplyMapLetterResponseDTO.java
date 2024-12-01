package postman.bottler.mapletter.dto.response;

import java.time.LocalDateTime;

public record FindAllSentReplyMapLetterResponseDTO(
        Long letterId,
        String title,
        String source


) {
}
