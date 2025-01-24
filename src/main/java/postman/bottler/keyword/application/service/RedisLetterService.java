package postman.bottler.keyword.application.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
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

    @Value("${recommendation.limit.active-recommendations}")
    private int maxRecommendations;
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
        if (tempRecommendations.isEmpty()) {
            log.warn("사용자 [{}]에 대한 임시 추천 데이터가 없습니다.", userId);
            throw new TempRecommendationsNotFoundException("임시 추천 데이터가 없습니다. userId: " + userId);
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
        if (activeRecommendations.size() >= maxRecommendations) {
            activeRecommendations.remove(0);
        }
        activeRecommendations.add(letterId);
        redisTemplate.opsForValue().set(activeKey, activeRecommendations);
    }

    private boolean isValidLetter(Long letterId) {
        return letterService.letterExists(letterId);
    }

    private List<Long> getActiveRecommendations(String activeKey) {
        List<Long> activeRecommendations = redisTemplate.opsForValue().get(activeKey);
        if (activeRecommendations == null || activeRecommendations.isEmpty()) {
            activeRecommendations = new LinkedList<>();
        }
        return activeRecommendations;
    }

    private List<Long> getTempRecommendations(String tempKey) {
        List<Long> tempRecommendations = redisTemplate.opsForValue().get(tempKey);
        if (tempRecommendations == null || tempRecommendations.isEmpty()) {
            log.warn("Redis 키 [{}]에 대한 추천 데이터가 없습니다.", tempKey);
            return new ArrayList<>(); // 빈 리스트 반환
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

    public void saveReplyToRedis(Long letterId, String labelUrl, Long receiverId) {
        String key = "REPLY:" + receiverId;
        String value = ReplyType.KEYWORD + ":" + letterId + ":" + labelUrl;

        Long size = redisTemplateForReply.opsForList().size(key);
        int REDIS_SAVED_REPLY = 6;
        if (size != null && size >= REDIS_SAVED_REPLY) {
            redisTemplateForReply.opsForList().rightPop(key);
        }

        if (!Objects.requireNonNull(redisTemplateForReply.opsForList().range(key, 0, -1)).contains(value)) {
            redisTemplateForReply.opsForList().leftPush(key, value);
        }
    }

    public void deleteRecentReply(Long receiverId, Long replyLetterId, String label) {
        String key = "REPLY:" + receiverId;
        String value = ReplyType.KEYWORD + ":" + replyLetterId + ":" + label;

        redisTemplate.opsForList().remove(key, 1, value);
    }
}
