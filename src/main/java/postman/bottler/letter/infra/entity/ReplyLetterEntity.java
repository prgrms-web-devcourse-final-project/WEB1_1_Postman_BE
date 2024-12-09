package postman.bottler.letter.infra.entity;

import jakarta.persistence.Column;
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
    @Column(columnDefinition = "TEXT")
    private String content;
    private String font;
    private String paper;
    private String label;
    private Long letterId;
    private Long receiverId;
    private Long senderId;
    private boolean isDeleted;
    private boolean isBlocked;
    private LocalDateTime createdAt;

    @Builder
    private ReplyLetterEntity(String title, String content, String font, String paper,
                              String label, Long letterId, Long receiverId, Long senderId,
                              boolean isDeleted, boolean isBlocked, LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.font = font;
        this.paper = paper;
        this.label = label;
        this.letterId = letterId;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.isDeleted = isDeleted;
        this.isBlocked = isBlocked;
        this.createdAt = createdAt;
    }

    public static ReplyLetterEntity from(ReplyLetter replyLetter) {
        return ReplyLetterEntity.builder()
                .title(replyLetter.getTitle())
                .content(replyLetter.getContent())
                .font(replyLetter.getFont())
                .paper(replyLetter.getPaper())
                .label(replyLetter.getLabel())
                .letterId(replyLetter.getLetterId())
                .receiverId(replyLetter.getReceiverId())
                .senderId(replyLetter.getSenderId())
                .isDeleted(replyLetter.isDeleted())
                .isBlocked(replyLetter.isBlocked())
                .createdAt(replyLetter.getCreatedAt())
                .build();
    }

    public ReplyLetter toDomain() {
        return ReplyLetter.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .font(this.font)
                .paper(this.paper)
                .label(this.label)
                .letterId(this.letterId)
                .receiverId(this.receiverId)
                .senderId(this.senderId)
                .isDeleted(this.isDeleted)
                .isBlocked(this.isBlocked)
                .createdAt(this.createdAt)
                .build();
    }
}
