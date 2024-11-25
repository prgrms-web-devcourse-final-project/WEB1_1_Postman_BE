package postman.bottler.letter.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Letter {
    private Long id;
    private String content;
    private List<String> keywords;
    private String font;
    private String paper;
    private String label;
    private Long userId;
    private boolean isDeleted;
    private boolean isBlocked;
    private LocalDateTime createdAt;

    @Builder
    private Letter(Long id, String content, List<String> keywords, String font, String paper, String label,
                   Long userId, boolean isDeleted, boolean isBlocked, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.keywords = keywords;
        this.font = font;
        this.paper = paper;
        this.label = label;
        this.userId = userId;
        this.isDeleted = isDeleted;
        this.isBlocked = isBlocked;
        this.createdAt = createdAt;
    }
}
