package postman.bottler.keyword.application.service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.keyword.util.RedisLetterKeyUtil;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.application.dto.LetterBoxDTO;
import postman.bottler.letter.exception.LetterNotFoundException;
import postman.bottler.letter.application.service.LetterBoxService;
import postman.bottler.letter.application.service.LetterService;
import postman.bottler.notification.application.dto.request.RecommendNotificationRequestDTO;

@Service
@RequiredArgsConstructor
public class RedisLetterService {

    private final RedisTemplate<String, List<Long>> redisTemplate;
    private final LetterBoxService letterBoxService;
    private static final int MAX_RECOMMENDATIONS = 3;
    private final LetterService letterService;

    @Transactional
    public void saveRecommendationsTemp(Long userId, List<Long> recommendations) {
        String key = RedisLetterKeyUtil.getTempRecommendationKey(userId);
        redisTemplate.opsForValue().set(key, recommendations);
    }

    @Transactional
    public void saveDeveloperLetter(Long userId, List<Long> recommendations) {
        String key = RedisLetterKeyUtil.getActiveRecommendationKey(userId);
        redisTemplate.opsForValue().set(key, recommendations);
    }

    @Transactional
    public RecommendNotificationRequestDTO updateRecommendationsFromTemp(Long userId) {
        String tempKey = RedisLetterKeyUtil.getTempRecommendationKey(userId);
        String activeKey = RedisLetterKeyUtil.getActiveRecommendationKey(userId);

        List<Long> tempRecommendations = getTempRecommendations(tempKey);
        if (tempRecommendations == null) {
            return null;
        }
        List<Long> activeRecommendations = getActiveRecommendations(activeKey);

        Long recommendId = findFirstValidLetter(tempRecommendations);
        updateActiveRecommendations(recommendId, activeRecommendations, activeKey);
        saveLetterToBox(userId, recommendId);
        Letter letter = letterService.findLetter(recommendId);

        redisTemplate.delete(tempKey);

        return RecommendNotificationRequestDTO.of(userId, recommendId, letter.getLabel());
    }

    private void saveLetterToBox(Long userId, Long letterId) {
        letterBoxService.saveLetter(LetterBoxDTO.of(
                userId, letterId, LetterType.LETTER, BoxType.RECEIVE, LocalDateTime.now()
        ));
    }

    private Long findFirstValidLetter(List<Long> recommendations) {
        return recommendations.stream()
                .filter(this::isValidLetter)
                .findFirst()
                .orElseThrow(() -> new LetterNotFoundException("No valid letters found in temp recommendations."));
    }

    private void updateActiveRecommendations(Long letterId, List<Long> activeRecommendations, String activeKey) {
        if (activeRecommendations.size() >= MAX_RECOMMENDATIONS) {
            activeRecommendations.remove(0);
        }
        activeRecommendations.add(letterId);
        redisTemplate.opsForValue().set(activeKey, activeRecommendations);
    }

    private boolean isValidLetter(Long letterId) {
        return letterService.existsLetter(letterId);
    }

    @NotNull
    private List<Long> getActiveRecommendations(String activeKey) {
        List<Long> activeRecommendations = redisTemplate.opsForValue().get(activeKey);
        if (activeRecommendations == null) {
            activeRecommendations = new LinkedList<>();
        }
        return activeRecommendations;
    }

    private List<Long> getTempRecommendations(String tempKey) {
        List<Long> tempRecommendations = redisTemplate.opsForValue().get(tempKey);
        if (tempRecommendations == null || tempRecommendations.isEmpty()) {
            return null;
        }
        return tempRecommendations;
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
