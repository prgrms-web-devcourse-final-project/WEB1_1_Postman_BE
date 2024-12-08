package postman.bottler.keyword.service;

import java.util.List;
import postman.bottler.keyword.domain.LetterKeyword;

public interface LetterKeywordRepository {
    void saveAll(List<LetterKeyword> letterKeywords);

    List<LetterKeyword> getKeywordsByLetterId(Long userId);

    List<Long> getMatchedLetters(List<String> userKeywords, List<Long> letterIds, int limit);

    void markKeywordsAsDeleted(List<Long> letterIds);
}
