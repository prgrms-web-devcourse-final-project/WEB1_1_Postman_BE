package postman.bottler.keyword.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import postman.bottler.keyword.application.repository.LetterKeywordRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendService {

    private final LetterKeywordRepository letterKeywordRepository;

    public List<Long> getRecommendedLetters(List<String> userKeywords, List<Long> letterIds, int limit) {
        log.debug("추천 편지 조회 요청: userKeywords={}, 제외할 letterIds={}, 추천 개수 limit={}", userKeywords, letterIds, limit);

        List<Long> recommendedLetters = letterKeywordRepository.getMatchedLetters(userKeywords, letterIds, limit);

        if (recommendedLetters.isEmpty()) {
            log.warn("추천할 편지가 없음: userKeywords={}", userKeywords);
        } else {
            log.info("추천 편지 조회 완료: 추천된 편지 개수={}, userKeywords={}", recommendedLetters.size(), userKeywords);
        }

        return recommendedLetters;
    }
}
