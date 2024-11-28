package postman.bottler.letter.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
}
