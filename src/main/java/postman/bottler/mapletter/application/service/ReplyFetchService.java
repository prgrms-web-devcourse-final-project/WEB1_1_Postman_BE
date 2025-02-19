package postman.bottler.mapletter.application.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import postman.bottler.mapletter.application.dto.ReplyProjectDTO;
import postman.bottler.mapletter.application.repository.ReplyMapLetterRepository;
import postman.bottler.mapletter.infra.ReplyLetterRedisRepository;

@Service
@RequiredArgsConstructor
public class ReplyFetchService {
    private final ReplyLetterRedisRepository replyLetterRedisRepository;
    private final ReplyMapLetterRepository replyMapLetterRepository;

    private static final int REDIS_SAVED_REPLY = 6;

    public void fetchRecentReply(Long userId) {
        List<Object> redisValues = replyLetterRedisRepository.getRecentReplies(userId);
        int fetchItemSize = REDIS_SAVED_REPLY - (redisValues == null ? 0 : redisValues.size());

        // Redis에 저장된 값이 부족하면 DB에서 조회 후 Redis에 저장
        if (fetchItemSize > 0) {
            List<ReplyProjectDTO> recentMapKeywordReplyByUserId =
                    replyMapLetterRepository.findRecentMapKeywordReplyByUserId(userId, fetchItemSize);

            List<ReplyProjectDTO> reversedList = new ArrayList<>(recentMapKeywordReplyByUserId);
            Collections.reverse(reversedList);

            List<Object> existingValues = replyLetterRedisRepository.getRecentReplies(userId);

            for (ReplyProjectDTO reply : reversedList) {
                String tempValue = reply.getType() + ":" + reply.getId() + ":" + reply.getLabel();

                if (existingValues == null || !existingValues.contains(tempValue)) {
                    replyLetterRedisRepository.saveRecentReply(userId, reply.getType(), reply.getId(),
                            reply.getLabel());
                }
            }
        }
    }
}
