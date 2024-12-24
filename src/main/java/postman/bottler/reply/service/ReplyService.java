package postman.bottler.reply.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.mapletter.application.service.MapLetterService;
import postman.bottler.reply.dto.ReplyType;
import postman.bottler.reply.dto.response.ReplyResponseDTO;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final MapLetterService mapLetterService;
    private static final int REDIS_SAVED_REPLY = 6;

    @Transactional(readOnly = true)
    public List<ReplyResponseDTO> findRecentReplyLetters(Long userId) {
        String key = "REPLY:" + userId;
        List<Object> values = redisTemplate.opsForList().range(key, 0, 2);

        int fetchItemSize = REDIS_SAVED_REPLY - (values == null ? 0 : values.size());

        if (values == null || values.size() < 3) {
            mapLetterService.fetchRecentReply(userId, fetchItemSize);
            values = redisTemplate.opsForList().range(key, 0, 2);
        }

        assert values != null;
        return values.stream()
                .map(value -> {
                    String[] parts = value.toString().split(":");
                    return ReplyResponseDTO.from(ReplyType.valueOf(parts[0]), parts[2], Long.parseLong(parts[1]));
                })
                .collect(Collectors.toList());
    }
}
