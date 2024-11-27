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
import postman.bottler.letter.domain.Letter;

@Entity
@Table(name = "letters")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String font;
    private String paper;
    private String label;
    private String profile;
    private Long userId;
    private boolean isDeleted;
    private boolean isBlocked;
    private LocalDateTime createdAt;

    @Builder
    private LetterEntity(String title, String content, String font, String paper, String label, String profile,
                         Long userId, boolean isDeleted, boolean isBlocked, LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.font = font;
        this.paper = paper;
        this.label = label;
        this.profile = profile;
        this.userId = userId;
        this.isDeleted = isDeleted;
        this.isBlocked = isBlocked;
        this.createdAt = createdAt;
    }

    public static LetterEntity from(Letter letter) {
        return LetterEntity.builder()
                .title(letter.getTitle())
                .content(letter.getContent())
                .createdAt(letter.getCreatedAt())
                .font(letter.getFont())
                .paper(letter.getPaper())
                .label(letter.getLabel())
                .profile(letter.getProfile())
                .userId(letter.getUserId())
                .isDeleted(letter.isDeleted())
                .isBlocked(letter.isBlocked())
                .createdAt(letter.getCreatedAt())
                .build();
    }

    public Letter toDomain() {
        return Letter.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .font(this.font)
                .paper(this.paper)
                .label(this.label)
                .profile(this.profile)
                .userId(this.userId)
                .isDeleted(this.isDeleted)
                .isBlocked(this.isBlocked)
                .createdAt(this.createdAt)
                .build();
    }

}
