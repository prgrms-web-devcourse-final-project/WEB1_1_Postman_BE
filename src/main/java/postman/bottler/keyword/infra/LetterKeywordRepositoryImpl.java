package postman.bottler.keyword.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.keyword.domain.LetterKeyword;
import postman.bottler.keyword.infra.entity.LetterKeywordEntity;
import postman.bottler.keyword.infra.entity.QLetterKeywordEntity;
import postman.bottler.keyword.service.LetterKeywordRepository;

@Repository
@RequiredArgsConstructor
public class LetterKeywordRepositoryImpl implements LetterKeywordRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public void saveAll(List<LetterKeyword> letterKeywords) {
        for (LetterKeyword letterKeyword : letterKeywords) {
            entityManager.persist(LetterKeywordEntity.from(letterKeyword));
        }
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public List<LetterKeyword> getKeywordsByLetterId(Long letterId) {
        QLetterKeywordEntity letterKeywordEntity = QLetterKeywordEntity.letterKeywordEntity;

        List<LetterKeywordEntity> letterKeywordEntities = queryFactory
                .selectFrom(letterKeywordEntity)
                .where(letterKeywordEntity.letterId.eq(letterId))
                .fetch();

        return letterKeywordEntities.stream()
                .map(LetterKeywordEntity::toDomain)
                .toList();
    }
}
