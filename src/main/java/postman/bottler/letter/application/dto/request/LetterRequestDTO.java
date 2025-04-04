package postman.bottler.letter.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import postman.bottler.letter.domain.Letter;

public record LetterRequestDTO(@NotNull(message = "편지 제목은 Null이 될 수 없습니다.") String title,
                               @NotBlank(message = "편지 내용은 필수입니다.") String content,
                               @NotNull(message = "키워드는 Null이 될 수 없습니다.") List<String> keywords,
                               @NotBlank(message = "글씨체는 필수입니다.") String font,
                               @NotBlank(message = "편지지는 필수입니다.") String paper,
                               @NotBlank(message = "라벨은 필수입니다.") String label
) {
    public Letter toDomain(Long userId) {
        return Letter.builder()
                .title(validateTitle(title))
                .content(content)
                .font(font)
                .paper(paper)
                .label(label)
                .userId(userId)
                .isDeleted(false)
                .isBlocked(false)
                .build();
    }

    private static String validateTitle(String title) {
        return (title == null || title.trim().isEmpty()) ? "무제" : title;
    }
}
