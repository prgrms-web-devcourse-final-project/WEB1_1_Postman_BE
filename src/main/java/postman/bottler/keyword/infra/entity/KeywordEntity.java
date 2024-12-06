package postman.bottler.keyword.infra.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import postman.bottler.keyword.domain.Keyword;

@Entity
@Table(name = "keywords")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KeywordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String keyword;
    private String category;

    public Keyword toDomain() {
        return Keyword.builder()
                .id(id)
                .keyword(keyword)
                .category(category)
                .build();
    }
}
