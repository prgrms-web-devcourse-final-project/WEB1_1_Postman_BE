package postman.bottler.keyword.service;

import java.util.List;
import postman.bottler.keyword.domain.LetterKeyword;

public interface LetterKeywordRepository {
    void saveAll(List<LetterKeyword> letterKeywords);
}
