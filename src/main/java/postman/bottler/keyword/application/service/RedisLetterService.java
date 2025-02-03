package postman.bottler.keyword.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.keyword.util.RedisLetterKeyUtil;
import postman.bottler.letter.application.dto.LetterBoxDTO;
import postman.bottler.letter.application.service.LetterBoxService;
import postman.bottler.letter.application.service.LetterService;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.exception.LetterNotFoundException;
import postman.bottler.letter.exception.TempRecommendationsNotFoundException;
import postman.bottler.notification.application.dto.request.RecommendNotificationRequestDTO;
import postman.bottler.reply.application.dto.ReplyType;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisLetterService {

    private final RedisTemplate<String, List<Long>> redisTemplate;
    private final RedisTemplate<String, Object> redisTemplateForReply;
    private final LetterBoxService letterBoxService;
    private final LetterService letterService;

    @Value("${recommendation.limit.active-recommendations}")
    private int maxRecommendations;

    @Value("${recommendation.saved-replies}")
    private int redisSavedReply;

    @Transactional
    public void saveTempRecommendations(Long userId, List<Long> recommendations) {
        String key = getTempRecommendationKey(userId);
        redisTemplate.opsForValue().set(key, recommendations);
    }

    @Transactional
    public void saveDeveloperLetter(Long userId, List<Long> recommendations) {
        String key = getActiveRecommendationKey(userId);
        redisTemplate.opsForValue().set(key, recommendations);
    }

    public void saveReplyToRedis(Long letterId, String labelUrl, Long receiverId) {
        String key = getReplyKey(receiverId);
        String value = getReplyValue(letterId, labelUrl);

        Long size = redisTemplateForReply.opsForList().size(key);
        if (size != null && size >= redisSavedReply) {
            redisTemplateForReply.opsForList().rightPop(key);
        }

        if (!Objects.requireNonNull(redisTemplateForReply.opsForList().range(key, 0, -1)).contains(value)) {
            redisTemplateForReply.opsForList().leftPush(key, value);
        }
    }

    public List<Long> fetchActiveRecommendations(Long userId) {
        String activeKey = getActiveRecommendationKey(userId);
        return fetchRecommendations(activeKey);
    }

    public List<Long> fetchTempRecommendations(Long userId) {
        String tempKey = getTempRecommendationKey(userId);
        return fetchRecommendations(tempKey);
    }

    @Transactional
    public RecommendNotificationRequestDTO updateRecommendationsFromTemp(Long userId) {
        List<Long> tempRecommendations = fetchTempRecommendations(userId);
        List<Long> activeRecommendations = fetchActiveRecommendations(userId);

        Long recommendId = processRecommendations(userId, tempRecommendations, activeRecommendations);
        return createRecommendNotification(userId, recommendId);
    }

    public void deleteRecentReply(Long receiverId, Long replyLetterId, String label) {
        String key = getReplyKey(receiverId);
        String value = getReplyValue(replyLetterId, label);

        redisTemplate.opsForList().remove(key, 1, value);
    }

    private String getTempRecommendationKey(Long userId) {
        return RedisLetterKeyUtil.getTempRecommendationKey(userId);
    }

    private String getActiveRecommendationKey(Long userId) {
        return RedisLetterKeyUtil.getActiveRecommendationKey(userId);
    }

    private String getReplyKey(Long receiverId) {
        return RedisLetterKeyUtil.getReplyKey(receiverId);
    }

    private String getReplyValue(Long letterId, String labelUrl) {
        return createReplyValue(letterId, labelUrl);
    }

    private String createReplyValue(Long letterId, String labelUrl) {
        return ReplyType.KEYWORD + ":" + letterId + ":" + labelUrl;
    }

    private List<Long> fetchRecommendations(String key) {
        List<Long> recommendations = redisTemplate.opsForValue().get(key);
        validateRecommendations(key, recommendations);
        return recommendations;
    }

    private void validateRecommendations(String key, List<Long> recommendations) {
        if (recommendations == null || recommendations.isEmpty()) {
            throw new TempRecommendationsNotFoundException("추천 데이터가 없습니다. key: " + key);
        }
    }

    private Long processRecommendations(Long userId, List<Long> tempRecommendations, List<Long> activeRecommendations) {
        Long recommendId = findFirstValidLetter(tempRecommendations);
        updateActiveRecommendations(recommendId, activeRecommendations, getActiveRecommendationKey(userId));
        saveLetterToBox(userId, recommendId);
        redisTemplate.delete(getTempRecommendationKey(userId));
        return recommendId;
    }

    private Long findFirstValidLetter(List<Long> recommendations) {
        return recommendations.stream()
                .filter(this::isValidLetter)
                .findFirst()
                .orElseThrow(() -> new LetterNotFoundException("No valid letters found in temp recommendations."));
    }

    private boolean isValidLetter(Long letterId) {
        return letterService.letterExists(letterId);
    }

    private void updateActiveRecommendations(Long letterId, List<Long> activeRecommendations, String activeKey) {
        if (activeRecommendations.size() >= maxRecommendations) {
            activeRecommendations.remove(0);
        }
        activeRecommendations.add(letterId);
        redisTemplate.opsForValue().set(activeKey, activeRecommendations);
    }

    private void saveLetterToBox(Long userId, Long letterId) {
        letterBoxService.saveLetter(LetterBoxDTO.of(
                userId, letterId, LetterType.LETTER, BoxType.RECEIVE, LocalDateTime.now()
        ));
    }

    private RecommendNotificationRequestDTO createRecommendNotification(Long userId, Long recommendId) {
        Letter letter = letterService.findLetter(recommendId);
        return RecommendNotificationRequestDTO.of(userId, recommendId, letter.getLabel());
    }
}
