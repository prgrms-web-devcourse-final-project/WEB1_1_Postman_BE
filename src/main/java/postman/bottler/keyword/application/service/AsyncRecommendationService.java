package postman.bottler.keyword.application.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import postman.bottler.letter.application.service.LetterBoxService;

@Service
@RequiredArgsConstructor
public class AsyncRecommendationService {

    private final RecommendService recommendService;
    private final UserKeywordService userKeywordService;
    private final LetterBoxService letterBoxService;
    private static final int RECOMMENDATION_LIMIT = 10;
    private final RedisLetterService redisLetterService;

    @Async
    public void processRecommendationForUser(Long userId) {
        List<String> keywords = userKeywordService.getKeywordsByUserId(userId);
        List<Long> letterIds = letterBoxService.findReceivedLettersByUserId(userId);
        List<Long> recommendedLetters = recommendService.getRecommendedLetters(keywords, letterIds,
                RECOMMENDATION_LIMIT);

        redisLetterService.saveRecommendationsTemp(userId, recommendedLetters);
        CompletableFuture.completedFuture(null);
    }
}
