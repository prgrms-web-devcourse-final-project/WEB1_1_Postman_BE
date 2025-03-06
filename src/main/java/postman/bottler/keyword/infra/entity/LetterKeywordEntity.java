package postman.bottler.keyword.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import postman.bottler.keyword.domain.LetterKeyword;

@Entity
@Table(name = "letter_keyword")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterKeywordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long letterId;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    public LetterKeywordEntity(Long letterId, String keyword, boolean isDeleted) {
        this.letterId = letterId;
        this.keyword = keyword;
        this.isDeleted = isDeleted;
    }

    public static LetterKeywordEntity from(LetterKeyword letterKeyword) {
        return LetterKeywordEntity.builder()
                .letterId(letterKeyword.getLetterId())
                .keyword(letterKeyword.getKeyword())
                .isDeleted(letterKeyword.isDeleted())
                .build();
    }

    public LetterKeyword toDomain() {
        return LetterKeyword.builder()
                .id(id)
                .letterId(letterId)
                .keyword(keyword)
                .isDeleted(isDeleted)
                .build();
    }
}
