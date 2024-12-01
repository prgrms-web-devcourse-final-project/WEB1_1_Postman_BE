package postman.bottler.mapletter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.ReplyMapLetter;

import java.util.List;

@Repository
public interface ReplyMapLetterRepository {
    ReplyMapLetter save(ReplyMapLetter replyMapLetter);

    List<ReplyMapLetter> findActiveReplyMapLettersBySourceUserId(Long userId);

    Page<ReplyMapLetter> findReplyMapLettersBySourceLetterId(Long letterId, Pageable pageable);

    ReplyMapLetter findById(Long letterId);

    boolean findByLetterIdAndUserId(Long letterId, Long userId);

    void letterBlock(Long letterId);

    void softDelete(Long letterId);
}
