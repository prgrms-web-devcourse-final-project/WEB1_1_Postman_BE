package postman.bottler.letter.infra.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import postman.bottler.letter.domain.Letter;

@Entity
@Table(name = "letters")
@RequiredArgsConstructor
public class LetterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String font;
    private String paper;
    private String label;
    private Long userId;
    private boolean isDeleted;
    private boolean isBlocked;
    private LocalDateTime createdAt;

    @Builder
    private LetterEntity(String content, String font, String paper, String label, Long userId,
                         boolean isDeleted, boolean isBlocked, LocalDateTime createdAt) {
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
                .content(letter.getContent())
                .createdAt(letter.getCreatedAt())
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
                .id(this.id)
                .content(this.content)
                .font(this.font)
                .paper(this.paper)
                .label(this.label)
                .userId(this.userId)
                .isDeleted(this.isDeleted)
                .isBlocked(this.isBlocked)
                .createdAt(this.createdAt)
                .build();
    }

}
