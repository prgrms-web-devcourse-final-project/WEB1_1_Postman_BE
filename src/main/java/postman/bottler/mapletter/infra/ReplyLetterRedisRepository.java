package postman.bottler.mapletter.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.application.repository.RecentReplyStorage;

@Repository
@RequiredArgsConstructor
public class ReplyLetterRedisRepository implements RecentReplyStorage {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final int REDIS_SAVED_REPLY = 6;
    private static final String RECENT_REPLY_KEY_PREFIX = "REPLY:";

    @Override
    public List<Object> getRecentReplies(Long userId) {
        String key = createKey(userId);
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    @Override
    public void saveRecentReply(Long userId, String type, Long letterId, String labelUrl) {
        String key = createKey(userId);
        String value = createValue(type, letterId, labelUrl);

        Long size = redisTemplate.opsForList().size(key);
        if (size != null && size >= REDIS_SAVED_REPLY) {
            redisTemplate.opsForList().rightPop(key);
        }

        if (!redisTemplate.opsForList().range(key, 0, -1).contains(value)) {
            redisTemplate.opsForList().leftPush(key, value);
        }
    }

    @Override
    public void deleteRecentReply(Long userId, String type, Long letterId, String labelUrl) {
        String key = createKey(userId);
        String value = createValue(type, letterId, labelUrl);
        redisTemplate.opsForList().remove(key, 1, value);
    }

    private String createKey(Long userId) {
        return RECENT_REPLY_KEY_PREFIX + userId;
    }

    private String createValue(String type, Long letterId, String labelUrl) {
        return type + ":" + letterId + ":" + labelUrl;
    }
}
