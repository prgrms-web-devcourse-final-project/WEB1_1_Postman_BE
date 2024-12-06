package postman.bottler.keyword.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import postman.bottler.keyword.util.RedisLetterKeyUtil;
import postman.bottler.letter.service.LetterBoxService;

@Service
@RequiredArgsConstructor
public class RedisLetterService {

    private final RedisTemplate<String, List<Long>> redisTemplate;
    private final LetterBoxService letterBoxService;

    public void saveRecommendationsTemp(Long userId, List<Long> recommendations) {
        String key = RedisLetterKeyUtil.getTempRecommendationKey(userId);
        redisTemplate.opsForValue().set(key, recommendations);
    }

    public void updateRecommendationsFromTemp(Long userId) {
        String tempKey = RedisLetterKeyUtil.getTempRecommendationKey(userId);
        String activeKey = RedisLetterKeyUtil.getActiveRecommendationKey(userId);

        List<Long> tempRecommendations = redisTemplate.opsForValue().get(tempKey);
        letterBoxService.saveRecommendedLetter(tempRecommendations, userId);
        if (tempRecommendations != null) {
            redisTemplate.opsForValue().set(activeKey, tempRecommendations);
            redisTemplate.delete(tempKey);
        }
    }

    public List<Long> getRecommendations(Long userId) {
        String activeKey = RedisLetterKeyUtil.getActiveRecommendationKey(userId);
        return redisTemplate.opsForValue().get(activeKey);
    }
}
