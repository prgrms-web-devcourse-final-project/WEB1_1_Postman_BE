package postman.bottler.letter.dto.request;

import java.time.LocalDateTime;
import postman.bottler.letter.domain.ReplyLetter;

public record ReplyLetterRequestDTO(
        String content,
        String font,
        String paper,
        String label
) {
    public ReplyLetter toDomain(String title, Long letterId, Long userId, String profile) {
        return ReplyLetter.builder()
                .title(title)
                .content(this.content)
                .font(this.font)
                .paper(this.paper)
                .label(this.label)
                .profile(profile)
                .letterId(letterId)
                .userId(userId)
                .isDeleted(false)
                .isBlocked(false)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
