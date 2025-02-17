package postman.bottler.mapletter.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReplyLetterRedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final int REDIS_SAVED_REPLY = 6;

    public void saveRecentReply(String key, String value) {
        Long size = redisTemplate.opsForList().size(key);
        if (size != null && size >= REDIS_SAVED_REPLY) {
            redisTemplate.opsForList().rightPop(key);
        }

        if (!redisTemplate.opsForList().range(key, 0, -1).contains(value)) {
            redisTemplate.opsForList().leftPush(key, value);
        }
    }

    public void deleteRecentReply(String key, String value) {
        redisTemplate.opsForList().remove(key, 1, value);
    }

    public List<Object> getRecentReplies(Long userId) {
        String key = "REPLY:" + userId;
        return redisTemplate.opsForList().range(key, 0, -1);
    }
}
