package postman.bottler.mapletter.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.ReplyMapLetter;

@Repository
public interface ReplyMapLetterRepository {
    ReplyMapLetter save(ReplyMapLetter replyMapLetter);

    Page<ReplyMapLetter> findActiveReplyMapLettersBySourceUserId(Long userId, Pageable pageable);

    Page<ReplyMapLetter> findReplyMapLettersBySourceLetterId(Long letterId, Pageable pageable);

    ReplyMapLetter findById(Long letterId);

    boolean findByLetterIdAndUserId(Long letterId, Long userId);

    void letterBlock(Long letterId);

    void softDelete(Long letterId);

    Page<ReplyMapLetter> findAllSentReplyByUserId(Long userId, PageRequest pageRequest);

    List<ReplyMapLetter> findRecentReplyByUserId(Long userId, long itemsToFetch);
}
