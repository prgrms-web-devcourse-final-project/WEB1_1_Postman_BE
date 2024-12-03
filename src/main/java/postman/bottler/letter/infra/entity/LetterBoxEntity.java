package postman.bottler.letter.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterBox;
import postman.bottler.letter.domain.LetterType;

@Entity
@Table(name = "letter_box")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterBoxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long letterId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LetterType letterType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoxType boxType;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    private LetterBoxEntity(Long userId, Long letterId, LetterType letterType, BoxType boxType, boolean isDeleted,
                            LocalDateTime createdAt) {
        this.userId = userId;
        this.letterId = letterId;
        this.letterType = letterType;
        this.boxType = boxType;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }

    public static LetterBoxEntity from(LetterBox letterBox) {
        return LetterBoxEntity.builder()
                .userId(letterBox.getUserId())
                .letterId(letterBox.getLetterId())
                .letterType(letterBox.getLetterType())
                .boxType(letterBox.getBoxType())
                .isDeleted(letterBox.isDeleted())
                .createdAt(letterBox.getCreatedAt())
                .build();
    }

    public LetterBox toDomain() {
        return LetterBox.builder()
                .id(this.id)
                .userId(this.userId)
                .letterId(this.letterId)
                .letterType(this.letterType)
                .boxType(this.boxType)
                .isDeleted(this.isDeleted)
                .createdAt(this.createdAt)
                .build();
    }
}
