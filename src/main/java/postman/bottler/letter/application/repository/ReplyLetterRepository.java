package postman.bottler.letter.application.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import postman.bottler.letter.domain.ReplyLetter;

public interface ReplyLetterRepository {
    ReplyLetter save(ReplyLetter replyLetter);

    Page<ReplyLetter> findAllByLetterIdAndReceiverId(Long letterId, Long receiverId, Pageable pageable);

    Optional<ReplyLetter> findById(Long replyLetterId);

    List<ReplyLetter> findAllByIds(List<Long> letterIds);

    void deleteByIds(List<Long> letterIds);

    void blockReplyLetterById(Long replyLetterId);

    boolean existsByLetterIdAndSenderId(Long letterId, Long senderId);

    boolean existsByIdAndSenderId(Long replyLetterId, Long userId);
}
