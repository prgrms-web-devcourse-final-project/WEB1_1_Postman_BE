package postman.bottler.keyword.service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import postman.bottler.keyword.util.RedisLetterKeyUtil;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.dto.LetterBoxDTO;
import postman.bottler.letter.service.LetterBoxService;
import postman.bottler.letter.service.LetterService;

@Service
@RequiredArgsConstructor
public class RedisLetterService {

    private final RedisTemplate<String, List<Long>> redisTemplate;
    private final LetterBoxService letterBoxService;
    private static final int MAX_RECOMMENDATIONS = 3;
    private final LetterService letterService;

    public void saveRecommendationsTemp(Long userId, List<Long> recommendations) {
        String key = RedisLetterKeyUtil.getTempRecommendationKey(userId);
        redisTemplate.opsForValue().set(key, recommendations);
    }

    public void updateRecommendationsFromTemp(Long userId) {
        String tempKey = RedisLetterKeyUtil.getTempRecommendationKey(userId);
        String activeKey = RedisLetterKeyUtil.getActiveRecommendationKey(userId);

        List<Long> tempRecommendations = redisTemplate.opsForValue().get(tempKey);
        if (tempRecommendations == null || tempRecommendations.isEmpty()) {
            return;
        }

        List<Long> activeRecommendations = redisTemplate.opsForValue().get(activeKey);
        if (activeRecommendations == null) {
            activeRecommendations = new LinkedList<>();
        }

        for (Long letterId : tempRecommendations) {
            boolean letterExists = letterService.checkLetterExists(letterId);
            if (letterExists) {
                if (activeRecommendations.size() >= MAX_RECOMMENDATIONS) {
                    activeRecommendations.remove(0);
                }
                activeRecommendations.add(letterId);
                letterBoxService.saveLetter(LetterBoxDTO.of(
                        userId, letterId, LetterType.LETTER, BoxType.RECEIVE, LocalDateTime.now()
                ));

                redisTemplate.opsForValue().set(activeKey, activeRecommendations);
                break;
            }
        }

        redisTemplate.delete(tempKey);
    }

    public List<Long> getRecommendations(Long userId) {
        String activeKey = RedisLetterKeyUtil.getActiveRecommendationKey(userId);
        return redisTemplate.opsForValue().get(activeKey);
    }

    public List<Long> getRecommendedTemp(Long userId) {
        String tempKey = RedisLetterKeyUtil.getTempRecommendationKey(userId);
        return redisTemplate.opsForValue().get(tempKey);
    }
}
