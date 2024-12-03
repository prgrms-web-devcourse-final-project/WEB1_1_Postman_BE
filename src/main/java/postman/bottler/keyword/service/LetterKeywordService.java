package postman.bottler.keyword.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.keyword.domain.LetterKeyword;

@Service
@RequiredArgsConstructor
public class LetterKeywordService {

    private final LetterKeywordRepository letterKeywordRepository;

    @Transactional
    public void createLetterKeywords(Long letterId, List<String> keywords) {
        List<LetterKeyword> letterKeywords = keywords.stream()
                .map(keyword -> LetterKeyword.from(letterId, keyword))
                .toList();

        letterKeywordRepository.saveAll(letterKeywords);
    }

    public void getKeywords(Long userId) {
    }
}
