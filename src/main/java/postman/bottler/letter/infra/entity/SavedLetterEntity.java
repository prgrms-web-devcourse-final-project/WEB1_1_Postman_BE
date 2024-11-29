package postman.bottler.letter.infra.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import postman.bottler.letter.domain.SavedLetter;

@Entity
@Table(name = "saved_letters")
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class SavedLetterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long letterId;

    @Builder
    private SavedLetterEntity(Long userId, Long letterId) {
        this.userId = userId;
        this.letterId = letterId;
    }

    public static SavedLetterEntity from(SavedLetter savedLetter) {
        return SavedLetterEntity.builder()
                .userId(savedLetter.getUserId())
                .letterId(savedLetter.getLetterId())
                .build();
    }

    public SavedLetter toDomain() {
        return SavedLetter.builder()
                .id(this.id)
                .userId(this.userId)
                .letterId(this.letterId)
                .build();
    }
}
