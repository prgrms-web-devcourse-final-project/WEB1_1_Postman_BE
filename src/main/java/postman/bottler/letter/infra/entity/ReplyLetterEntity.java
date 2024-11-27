package postman.bottler.letter.infra.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import postman.bottler.letter.domain.ReplyLetter;

@Entity
@Table(name = "reply_letters")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyLetterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String font;
    private String paper;
    private String label;
    private String profile;
    private Long letterId;
    private Long userId;
    private boolean isDeleted;
    private boolean isBlocked;
    private LocalDateTime createdAt;

    @Builder
    private ReplyLetterEntity(String title, String content, String font, String paper,
                              String label, String profile, Long letterId, Long userId,
                              boolean isDeleted, boolean isBlocked, LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.font = font;
        this.paper = paper;
        this.label = label;
        this.profile = profile;
        this.letterId = letterId;
        this.userId = userId;
        this.isDeleted = false;
        this.isBlocked = false;
        this.createdAt = createdAt;
    }

    public static ReplyLetterEntity from(ReplyLetter replyLetter) {
        return ReplyLetterEntity.builder()
                .title(replyLetter.getTitle())
                .content(replyLetter.getContent())
                .font(replyLetter.getFont())
                .paper(replyLetter.getPaper())
                .label(replyLetter.getLabel())
                .profile(replyLetter.getProfile())
                .letterId(replyLetter.getLetterId())
                .userId(replyLetter.getUserId())
                .isDeleted(replyLetter.isDeleted())
                .isBlocked(replyLetter.isBlocked())
                .createdAt(replyLetter.getCreatedAt())
                .build();
    }

    public ReplyLetter toDomain() {
        return ReplyLetter.builder()
                .title(this.title)
                .content(this.content)
                .font(this.font)
                .paper(this.paper)
                .label(this.label)
                .profile(this.profile)
                .letterId(this.letterId)
                .userId(this.userId)
                .isDeleted(this.isDeleted)
                .isBlocked(this.isBlocked)
                .createdAt(this.createdAt)
                .build();
    }
}
