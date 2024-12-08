package postman.bottler.keyword.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final LetterKeywordRepository letterKeywordRepository;

    public List<Long> getRecommendedLetters(List<String> userKeywords, List<Long> letterIds, int limit) {
        return letterKeywordRepository.getMatchedLetters(userKeywords, letterIds, limit);
    }
}
