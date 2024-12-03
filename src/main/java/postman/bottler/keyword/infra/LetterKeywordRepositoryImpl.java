package postman.bottler.keyword.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.keyword.domain.LetterKeyword;
import postman.bottler.keyword.infra.entity.LetterKeywordEntity;
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
}
