package postman.bottler.mapletter.service;

import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.ReplyMapLetter;

import java.util.List;

@Repository
public interface ReplyMapLetterRepository {
    ReplyMapLetter save(ReplyMapLetter replyMapLetter);
    List<ReplyMapLetter> findActiveReplyMapLettersBySourceUserId(Long userId);
    List<ReplyMapLetter> findReplyMapLettersBySourceLetterId(Long letterId);

    ReplyMapLetter findById(Long letterId);

    boolean findByLetterIdAndUserId(Long letterId, Long userId);

    void letterBlock(Long letterId);
}
