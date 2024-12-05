package postman.bottler.keyword.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import postman.bottler.letter.service.LetterBoxService;

@Service
@RequiredArgsConstructor
public class AsyncRecommendationService {

    private final RecommendService recommendService;
    private final UserKeywordService userKeywordService;
    private final LetterBoxService letterBoxService;
    private final RedisTemplate<String, List<Long>> redisTemplate;
    private static final int RECOMMENDATION_LIMIT = 3;

    @Async
    public void processRecommendationForUser(Long userId) {
        List<String> keywords = userKeywordService.getKeywordsByUserId(userId);
        List<Long> letterIds = letterBoxService.getLettersByUserId(userId);
        List<Long> recommendedLetters = recommendService.getRecommendedLetters(keywords, letterIds,
                RECOMMENDATION_LIMIT);

        String redisKey = "user:" + userId + ":recommendations";
        redisTemplate.opsForValue().set(redisKey, recommendedLetters);

        CompletableFuture.completedFuture(null);
    }
}
