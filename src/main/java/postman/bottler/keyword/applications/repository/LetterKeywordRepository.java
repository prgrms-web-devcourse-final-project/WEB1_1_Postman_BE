package postman.bottler.keyword.applications.repository;

import java.util.List;
import postman.bottler.keyword.domain.LetterKeyword;

public interface LetterKeywordRepository {
    List<LetterKeyword> saveAll(List<LetterKeyword> letterKeywords);

    List<LetterKeyword> getKeywordsByLetterId(Long userId);

    List<Long> getMatchedLetters(List<String> userKeywords, List<Long> letterIds, int limit);

    void markKeywordsAsDeleted(List<Long> letterIds);

    List<LetterKeyword> getFrequentKeywords(List<Long> letterIds);
}
