package postman.bottler.letter.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.domain.ReplyLetter;
import postman.bottler.letter.dto.LetterBoxDTO;
import postman.bottler.letter.dto.ReceiverDTO;
import postman.bottler.letter.dto.request.PageRequestDTO;
import postman.bottler.letter.dto.request.ReplyLetterRequestDTO;
import postman.bottler.letter.dto.response.ReplyLetterHeadersResponseDTO;
import postman.bottler.letter.dto.response.ReplyLetterResponseDTO;
import postman.bottler.letter.exception.LetterNotFoundException;
import postman.bottler.reply.dto.ReplyType;

@Service
@RequiredArgsConstructor
public class ReplyLetterService {

    private final ReplyLetterRepository replyLetterRepository;
    private final LetterService letterService;
    private final LetterBoxService letterBoxService;
    private final RedisTemplate<String, Object> redisTemplate;

    private final int REDIS_SAVED_REPLY = 6;

    @Transactional
    public ReplyLetterResponseDTO createReplyLetter(Long letterId, ReplyLetterRequestDTO letterReplyRequestDTO,
                                                    Long senderId) {
        ReceiverDTO receiverInfo = letterService.getReceiverInfoById(letterId);
        String title = generateReplyTitle(receiverInfo.title());
        Long receiverId = receiverInfo.receiverId();

        String userProfile = getCurrentUserProfile();

        ReplyLetter replyLetter = replyLetterRepository.save(
                letterReplyRequestDTO.toDomain(title, letterId, receiverId, senderId, userProfile)
        );

        letterBoxService.saveLetter(
                LetterBoxDTO.of(senderId, replyLetter.getId(), LetterType.REPLY_LETTER, BoxType.SEND,
                        replyLetter.getCreatedAt()));
        letterBoxService.saveLetter(
                LetterBoxDTO.of(receiverId, replyLetter.getId(), LetterType.REPLY_LETTER, BoxType.RECEIVE,
                        replyLetter.getCreatedAt()));
        saveRecentReply(letterId, letterReplyRequestDTO.label(), receiverId);
        return ReplyLetterResponseDTO.from(replyLetter);
    }

    @Transactional(readOnly = true)
    public Page<ReplyLetterHeadersResponseDTO> getReplyLetterHeadersById(
            Long letterId, PageRequestDTO pageRequestDTO, Long receiverId
    ) {
        return replyLetterRepository.findAllByLetterId(letterId, receiverId, pageRequestDTO.toPageable())
                .map(ReplyLetterHeadersResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public ReplyLetterResponseDTO getReplyLetterDetail(Long replyLetterId) {
        ReplyLetter replyLetter = replyLetterRepository.findById(replyLetterId)
                .orElseThrow(() -> new LetterNotFoundException("답장 편지가 존재하지 않습니다."));
        return ReplyLetterResponseDTO.from(replyLetter);
    }

    @Transactional
    public void deleteReplyLetter(Long replyLetterId) {
        replyLetterRepository.delete(replyLetterId);
        deleteRecentReply(replyLetterId);
    }

    @Transactional
    public void deleteReplyLetters(List<Long> letterIds) {
        replyLetterRepository.deleteByIds(letterIds);
        for (Long letterId : letterIds) {
            deleteRecentReply(letterId);
        }
    }

    private String generateReplyTitle(String title) {
        return "RE: [" + title + "]";
    }

    private String getCurrentUserProfile() {
        return "url";
    }

    private void saveRecentReply(Long letterId, String labelUrl, Long sourceLetterCreateUserId) {
        String key = "REPLY:" + sourceLetterCreateUserId;
        String value = ReplyType.KEYWORD + ":" + letterId + ":" + labelUrl;

        Long size = redisTemplate.opsForList().size(key);
        if (size != null && size >= REDIS_SAVED_REPLY) {
            redisTemplate.opsForList().rightPop(key);
        }

        if (!redisTemplate.opsForList().range(key, 0, -1).contains(value)) {
            redisTemplate.opsForList().leftPush(key, value);
        }
    }

    private void deleteRecentReply(Long letterId) {
        ReplyLetter replyLetter = replyLetterRepository.findById(letterId)
                .orElseThrow(()-> new LetterNotFoundException("편지가 존재하지 않습니다."));

        String key = "REPLY:" + replyLetter.getReceiverId();
        String value = ReplyType.KEYWORD + ":" + letterId + ":" + replyLetter.getLabel();

        redisTemplate.opsForList().remove(key, 1, value);
    }
}
