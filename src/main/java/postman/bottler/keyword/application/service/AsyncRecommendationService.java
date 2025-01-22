package postman.bottler.keyword.application.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import postman.bottler.letter.application.service.LetterBoxService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncRecommendationService {

    private final RecommendService recommendService;
    private final UserKeywordService userKeywordService;
    private final LetterBoxService letterBoxService;
    private final RedisLetterService redisLetterService;

    @Value("${recommendation.limit.candidate}")
    private int recommendationCandidateLimit;

    @Async
    public CompletableFuture<String> processRecommendationForUser(Long userId) {
        log.info("사용자 [{}]의 추천 작업을 시작합니다.", userId);

        try {
            List<String> keywords = userKeywordService.getKeywordsByUserId(userId);
            List<Long> letterIds = letterBoxService.findReceivedLettersByUserId(userId);

            if (log.isDebugEnabled()) {
                log.debug("사용자 [{}]의 키워드: {}, 받은 편지: {}", userId, keywords, letterIds);
            }

            List<Long> recommendedLetters = recommendService.getRecommendedLetters(keywords, letterIds,
                    recommendationCandidateLimit);
            redisLetterService.saveRecommendationsTemp(userId, recommendedLetters);

            log.info("사용자 [{}]의 추천 작업이 성공적으로 완료되었습니다.", userId);
            return CompletableFuture.completedFuture("Success: 사용자 [" + userId + "] 작업 완료");
        } catch (Exception e) {
            log.error("사용자 [{}]의 추천 작업 중 예기치 못한 예외 발생: {}", userId, e.getMessage(), e);
            return CompletableFuture.completedFuture("Error: 사용자 [" + userId + "] 예외 발생");
        }
    }
}
