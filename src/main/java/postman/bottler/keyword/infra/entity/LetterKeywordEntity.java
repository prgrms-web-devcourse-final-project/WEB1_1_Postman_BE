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

    @Builder
    public LetterKeywordEntity(Long letterId, String keyword) {
        this.letterId = letterId;
        this.keyword = keyword;
    }

    public static LetterKeywordEntity from(LetterKeyword letterKeyword) {
        return LetterKeywordEntity.builder()
                .letterId(letterKeyword.getLetterId())
                .keyword(letterKeyword.getKeyword())
                .build();
    }
}
