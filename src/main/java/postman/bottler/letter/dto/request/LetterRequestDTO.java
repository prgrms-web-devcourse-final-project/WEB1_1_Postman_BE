package postman.bottler.letter.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import postman.bottler.letter.domain.Letter;

public record LetterRequestDTO(
        String title,
        String content,
        List<String> keywords,
        String font,
        String paper,
        String label
) {
    public Letter toDomain(Long userId, String profile) {
        return Letter.builder()
                .title(title)
                .content(this.content)
                .keywords(this.keywords)
                .font(this.font)
                .paper(this.paper)
                .label(this.label)
                .profile(profile)
                .userId(userId)
                .isDeleted(false)
                .isBlocked(false)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
