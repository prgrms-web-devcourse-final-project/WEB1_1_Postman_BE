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
import postman.bottler.letter.domain.Letter;

@Entity
@Table(name = "letters")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String font;
    private String paper;
    private String label;
    private Long userId;
    private boolean isDeleted;
    private boolean isBlocked;
    private LocalDateTime createdAt;

    @Builder
    private LetterEntity(String title, String content, String font, String paper, String label, Long userId,
                         boolean isDeleted, boolean isBlocked,
                         LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.font = font;
        this.paper = paper;
        this.label = label;
        this.userId = userId;
        this.isDeleted = isDeleted;
        this.isBlocked = isBlocked;
        this.createdAt = createdAt;
    }

    public static LetterEntity from(Letter letter) {
        return LetterEntity.builder()
                .title(letter.getTitle())
                .content(letter.getContent())
                .font(letter.getFont())
                .paper(letter.getPaper())
                .label(letter.getLabel())
                .userId(letter.getUserId())
                .isDeleted(letter.isDeleted())
                .isBlocked(letter.isBlocked())
                .createdAt(letter.getCreatedAt())
                .build();
    }

    public Letter toDomain() {
        return Letter.builder()
                .id(id)
                .title(title)
                .content(content)
                .font(font)
                .paper(paper)
                .label(label)
                .userId(userId)
                .isDeleted(isDeleted)
                .isBlocked(isBlocked)
                .createdAt(createdAt)
                .build();
    }

}
