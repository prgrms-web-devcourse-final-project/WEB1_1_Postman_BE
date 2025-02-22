package postman.bottler.letter.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import postman.bottler.letter.domain.RecommendedLetter;

@Entity
@Table(name = "recommended_letter")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendedLetterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long letterId;

    @Builder
    public RecommendedLetterEntity(Long userId, Long letterId) {
        this.userId = userId;
        this.letterId = letterId;
    }

    public static RecommendedLetterEntity from(RecommendedLetter recommendedLetter) {
        return RecommendedLetterEntity.builder()
                .userId(recommendedLetter.getUserId())
                .letterId(recommendedLetter.getLetterId())
                .build();
    }

    public RecommendedLetter toDomain() {
        return RecommendedLetter.builder()
                .id(id)
                .userId(userId)
                .letterId(letterId)
                .build();
    }
}
