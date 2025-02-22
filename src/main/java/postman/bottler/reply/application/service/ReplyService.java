package postman.bottler.reply.application.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.mapletter.application.service.ReplyFetchService;
import postman.bottler.reply.application.dto.ReplyType;
import postman.bottler.reply.application.dto.response.ReplyResponseDTO;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ReplyFetchService replyFetchService;
    private static final int REDIS_SAVED_REPLY = 6;

    @Transactional(readOnly = true)
    public List<ReplyResponseDTO> findRecentReplyLetters(Long userId) {
        String key = "REPLY:" + userId;
        List<Object> values = redisTemplate.opsForList().range(key, 0, 2);

        if (values == null || values.size() < 3) {
            replyFetchService.fetchRecentReply(userId);
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
