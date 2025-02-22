package postman.bottler.keyword.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.keyword.application.dto.RecommendedLetterDTO;
import postman.bottler.keyword.util.RedisLetterKeyUtil;
import postman.bottler.letter.application.dto.LetterBoxDTO;
import postman.bottler.letter.application.service.LetterBoxService;
import postman.bottler.letter.application.service.LetterService;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.domain.LetterType;
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
    private final RecommendedLetterService recommendedLetterService;

    @Value("${recommendation.limit.active-recommendations}")
    private int maxRecommendations;

    @Value("${recommendation.saved-replies}")
    private int redisSavedReply;

    public void saveTempRecommendations(Long userId, List<Long> recommendations) {
        String key = getTempRecommendationKey(userId);
        log.info("임시 추천 저장: userId={}, 추천 개수={}", userId, recommendations.size());

        redisTemplate.opsForValue().set(key, recommendations);
    }

    public void saveDeveloperLetter(Long userId, List<Long> recommendations) {
        String key = getActiveRecommendationKey(userId);
        log.info("개발자 추천 저장: userId={}, 추천 개수={}", userId, recommendations.size());

        redisTemplate.opsForValue().set(key, recommendations);
    }

    public void saveReplyToRedis(Long letterId, String labelUrl, Long receiverId) {
        String key = getReplyKey(receiverId);
        String value = getReplyValue(letterId, labelUrl);

        log.debug("Redis에 답장 저장 요청: receiverId={}, letterId={}, label={}", receiverId, letterId, labelUrl);

        Long size = redisTemplateForReply.opsForList().size(key);
        if (size != null && size >= redisSavedReply) {
            redisTemplateForReply.opsForList().rightPop(key);
        }

        if (!Objects.requireNonNull(redisTemplateForReply.opsForList().range(key, 0, -1)).contains(value)) {
            redisTemplateForReply.opsForList().leftPush(key, value);
            log.info("Redis에 답장 저장 완료: receiverId={}, letterId={}", receiverId, letterId);
        }
    }

    public List<Long> fetchActiveRecommendations(Long userId) {
        String activeKey = getActiveRecommendationKey(userId);
        return fetchRecommendations(activeKey);
    }

    public List<Long> fetchTempRecommendations(Long userId) {
        log.info("[userId={}] 임시 추천 편지 조회 시작", userId);
        String tempKey = getTempRecommendationKey(userId);
        return fetchRecommendations(tempKey);
    }

    @Transactional
    public Optional<RecommendNotificationRequestDTO> updateRecommendationsFromTemp(Long userId) {
        List<Long> tempRecommendations = fetchTempRecommendations(userId);
        List<Long> activeRecommendations = fetchActiveRecommendations(userId);

        return processRecommendations(userId, tempRecommendations, activeRecommendations)
                .map(recommendId -> {
                    log.info("추천 편지 업데이트 완료: userId={}, 선택된 추천 편지 ID={}", userId, recommendId);
                    return createRecommendNotification(userId, recommendId);
                })
                .or(() -> {
                    log.info("userId={}에 대한 유효한 추천이 없음. 추천을 건너뜁니다.", userId);
                    return Optional.empty();
                });
    }

    public void deleteRecentReply(Long receiverId, Long replyLetterId, String label) {
        String key = getReplyKey(receiverId);
        String value = getReplyValue(replyLetterId, label);

        log.info("Redis에서 답장 삭제 요청: receiverId={}, letterId={}", receiverId, replyLetterId);
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
        return ReplyType.KEYWORD + ":" + letterId + ":" + labelUrl;
    }

    private List<Long> fetchRecommendations(String key) {
        List<Long> recommendations = redisTemplate.opsForValue().get(key);
        validateRecommendations(recommendations);
        return recommendations;
    }

    private void validateRecommendations(List<Long> recommendations) {
        if (recommendations == null || recommendations.isEmpty()) {
            log.warn("추천 데이터가 없습니다.");
        }
    }

    private Optional<Long> processRecommendations(Long userId, List<Long> tempRecommendations,
                                                  List<Long> activeRecommendations) {
        String activeKey = getActiveRecommendationKey(userId);
        String tempRecommendationKey = getTempRecommendationKey(userId);

        return findFirstValidLetter(tempRecommendations)
                .map(recommendId -> {
                    updateActiveRecommendations(recommendId, activeRecommendations, activeKey);
                    saveLetterToBox(userId, recommendId);
                    saveLetterToRecommendedLetter(userId, recommendId);

                    redisTemplate.delete(tempRecommendationKey);
                    return recommendId;
                });
    }

    private Optional<Long> findFirstValidLetter(List<Long> recommendations) {
        if (recommendations == null || recommendations.isEmpty()) {
            return Optional.empty();
        }

        List<Long> validLetters = recommendations.stream()
                .filter(this::isValidLetter)
                .toList();

        if (validLetters.isEmpty()) {
            log.warn("추천할 편지가 모두 삭제됨. 새로운 추천이 필요함.");
            return Optional.empty();
        }

        return Optional.of(validLetters.get(0));
    }

    private boolean isValidLetter(Long letterId) {
        return letterService.existsLetterById(letterId);
    }

    private void updateActiveRecommendations(Long letterId, List<Long> activeRecommendations, String activeKey) {
        if (activeRecommendations.size() - maxRecommendations >= 0) {
            activeRecommendations.subList(0, activeRecommendations.size() - maxRecommendations).clear();
        }
        activeRecommendations.add(letterId);
        redisTemplate.opsForValue().set(activeKey, activeRecommendations);

        log.info("사용자 현재 추천 편지 업데이트 완료: 추천 추가된 편지 ID={}, 저장된 개수={}", letterId, activeRecommendations.size());
    }

    private void saveLetterToBox(Long userId, Long letterId) {
        LetterBoxDTO letterBoxDTO = LetterBoxDTO.of(userId, letterId, LetterType.LETTER, BoxType.RECEIVE,
                LocalDateTime.now());

        letterBoxService.saveLetter(letterBoxDTO);
        log.info("[추천 편지 보관함 저장 완료] userId={}, letterId={}", userId, letterId);
    }

    private void saveLetterToRecommendedLetter(Long userId, Long letterId) {
        RecommendedLetterDTO recommendedLetterDTO = RecommendedLetterDTO.of(userId, letterId);

        recommendedLetterService.saveRecommendedLetter(recommendedLetterDTO);
        log.info("[추천된 편지 저장 완료] userId={}, letterId={}", userId, letterId);
    }

    private RecommendNotificationRequestDTO createRecommendNotification(Long userId, Long recommendId) {
        Letter letter = letterService.findLetter(recommendId);
        return RecommendNotificationRequestDTO.of(userId, recommendId, letter.getLabel());
    }
}
