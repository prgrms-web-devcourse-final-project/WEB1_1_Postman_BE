package postman.bottler.keyword.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.keyword.applications.repository.KeywordRepository;
import postman.bottler.keyword.domain.Keyword;
import postman.bottler.keyword.infra.entity.KeywordEntity;

@Repository
@RequiredArgsConstructor
public class KeywordRepositoryImpl implements KeywordRepository {

    private final KeywordJpaRepository keywordJpaRepository;

    @Override
    public List<Keyword> getKeywords() {
        List<KeywordEntity> keywords = keywordJpaRepository.findAll();
        return keywords.stream()
                .map(KeywordEntity::toDomain)
                .toList();
    }
}
