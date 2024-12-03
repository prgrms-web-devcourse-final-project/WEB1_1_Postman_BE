package postman.bottler.reply.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import postman.bottler.reply.dto.LetterLogDTO;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public List<LetterLogDTO> findRecentReplyLetters(Long userId){
        String key="REPLY:"+userId;
        List<Object> objects=redisTemplate.opsForList().range(key, 0,3);
        assert objects != null;
        return objects.stream()
                .map(object -> objectMapper.convertValue(object, LetterLogDTO.class))
                .collect(Collectors.toList());
    }
}
