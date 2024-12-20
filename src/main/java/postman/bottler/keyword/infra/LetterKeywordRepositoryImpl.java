package postman.bottler.keyword.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.keyword.domain.LetterKeyword;
import postman.bottler.keyword.infra.entity.LetterKeywordEntity;
import postman.bottler.keyword.service.LetterKeywordRepository;

@Repository
@RequiredArgsConstructor
public class LetterKeywordRepositoryImpl implements LetterKeywordRepository {

    private final LetterKeywordQueryDslRepository queryDslRepository;
    private final LetterKeywordJdbcRepository jdbcRepository;

    @Override
    public List<LetterKeyword> saveAll(List<LetterKeyword> letterKeywords) {
        return jdbcRepository.batchInsertKeywords(letterKeywords);
    }

    @Override
    public List<LetterKeyword> getKeywordsByLetterId(Long letterId) {
        return queryDslRepository.findKeywordsByLetterId(letterId).stream()
                .map(LetterKeywordEntity::toDomain)
                .toList();
    }

    @Override
    public List<Long> getMatchedLetters(List<String> userKeywords, List<Long> letterIds, int limit) {
        return queryDslRepository.getMatchedLetters(userKeywords, letterIds, limit);
    }

    @Override
    public void markKeywordsAsDeleted(List<Long> letterIds) {
        jdbcRepository.batchUpdateIsDeleted(letterIds);
    }

    @Override
    public List<LetterKeyword> getFrequentKeywords(List<Long> letterIds) {
        return queryDslRepository.getFrequentKeywords(letterIds).stream()
                .map(LetterKeywordEntity::toDomain)
                .toList();
    }
}
