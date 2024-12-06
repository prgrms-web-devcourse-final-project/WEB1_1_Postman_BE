package postman.bottler.letter.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import postman.bottler.letter.domain.ReplyLetter;

public record ReplyLetterRequestDTO(
        @NotBlank(message = "편지 내용은 필수입니다.") String content,
        @NotBlank(message = "글씨체는 필수입니다.") String font,
        @NotBlank(message = "편지지는 필수입니다.") String paper,
        @NotBlank(message = "라벨은 필수입니다.") String label
) {
    public ReplyLetter toDomain(String title, Long letterId, Long receiverId, Long senderId) {
        return ReplyLetter.builder()
                .title(title)
                .content(this.content)
                .font(this.font)
                .paper(this.paper)
                .label(this.label)
                .letterId(letterId)
                .receiverId(receiverId)
                .senderId(senderId)
                .isDeleted(false)
                .isBlocked(false)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
