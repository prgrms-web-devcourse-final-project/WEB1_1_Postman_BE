package postman.bottler.keyword.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import postman.bottler.keyword.application.repository.LetterKeywordRepository;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final LetterKeywordRepository letterKeywordRepository;

    public List<Long> getRecommendedLetters(List<String> userKeywords, List<Long> letterIds, int limit) {
        return letterKeywordRepository.getMatchedLetters(userKeywords, letterIds, limit);
    }
}
