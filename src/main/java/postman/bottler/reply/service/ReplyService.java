package postman.bottler.reply.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import postman.bottler.reply.dto.ReplyType;
import postman.bottler.reply.dto.response.ReplyResponseDTO;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public List<ReplyResponseDTO> findRecentReplyLetters(Long userId){
        String key="REPLY:"+userId;
        List<Object> values=redisTemplate.opsForList().range(key, 0,3);

        return values.stream()
                .map(value->{
                    String[] parts=value.toString().split(":");
                    return ReplyResponseDTO.from(ReplyType.valueOf(parts[0]), parts[2], Long.parseLong(parts[1]));
                })
                .collect(Collectors.toList());
    }
}
