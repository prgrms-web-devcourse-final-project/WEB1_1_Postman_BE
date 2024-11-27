package postman.bottler.mapletter.infra.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import postman.bottler.mapletter.domain.ReplyMapLetter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reply_map_letter_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReplyMapLetterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyLetterId;
    private Long sourceLetterId;
    private String font;
    private String paper;
    private String label;
    private String content;
    private boolean isBlocked;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createUserId;

    @Builder
    public ReplyMapLetterEntity(Long replyLetterId, Long sourceLetterId, String font, String paper, String label,
                                boolean isBlocked, boolean isDeleted, LocalDateTime createdAt,
                                LocalDateTime updatedAt, Long createUserId, String content) {
        this.replyLetterId = replyLetterId;
        this.sourceLetterId = sourceLetterId;
        this.font = font;
        this.paper = paper;
        this.label = label;
        this.isBlocked = isBlocked;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createUserId = createUserId;
        this.content = content;
    }

    public static ReplyMapLetterEntity from(ReplyMapLetter replyMapLetter) {
        return ReplyMapLetterEntity.builder()
                .replyLetterId(replyMapLetter.getReplyLetterId())
                .sourceLetterId(replyMapLetter.getSourceLetterId())
                .font(replyMapLetter.getFont())
                .paper(replyMapLetter.getPaper())
                .label(replyMapLetter.getLabel())
                .isBlocked(replyMapLetter.isBlocked())
                .isDeleted(replyMapLetter.isDeleted())
                .createdAt(replyMapLetter.getCreatedAt())
                .updatedAt(replyMapLetter.getUpdatedAt())
                .createUserId(replyMapLetter.getCreateUserId())
                .content(replyMapLetter.getContent())
                .build();
    }

    public static ReplyMapLetter toDomain(ReplyMapLetterEntity replyMapLetter) {
        return ReplyMapLetter.builder()
                .replyLetterId(replyMapLetter.getReplyLetterId())
                .sourceLetterId(replyMapLetter.getSourceLetterId())
                .font(replyMapLetter.getFont())
                .paper(replyMapLetter.getPaper())
                .label(replyMapLetter.getLabel())
                .isBlocked(replyMapLetter.isBlocked())
                .isDeleted(replyMapLetter.isDeleted())
                .createdAt(replyMapLetter.getCreatedAt())
                .updatedAt(replyMapLetter.getUpdatedAt())
                .createUserId(replyMapLetter.getCreateUserId())
                .content(replyMapLetter.getContent())
                .build();
    }

    public void updateDelete(boolean isDeleted){
        this.isDeleted=isDeleted;
    }
}
