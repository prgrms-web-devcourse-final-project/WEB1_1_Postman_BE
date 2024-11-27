package postman.bottler.letter.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyLetter {
    private Long id;
    private String title;
    private String content;
    private String font;
    private String paper;
    private String label;
    private String profile;
    private Long letterId;
    private Long receiverId;
    private Long senderId;
    private boolean isDeleted;
    private boolean isBlocked;
    private LocalDateTime createdAt;

    @Builder
    private ReplyLetter(Long id, String title, String content, String font, String paper,
                        String label, String profile, Long letterId, Long receiverId, Long senderId,
                        boolean isDeleted, boolean isBlocked, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.font = font;
        this.paper = paper;
        this.label = label;
        this.profile = profile;
        this.letterId = letterId;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.isDeleted = isDeleted;
        this.isBlocked = isBlocked;
        this.createdAt = createdAt;
    }
}
