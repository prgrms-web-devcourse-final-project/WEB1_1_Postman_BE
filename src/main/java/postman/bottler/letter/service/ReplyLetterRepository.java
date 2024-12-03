package postman.bottler.letter.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import postman.bottler.letter.domain.ReplyLetter;

public interface ReplyLetterRepository {
    ReplyLetter save(ReplyLetter replyLetter);

    Page<ReplyLetter> findAll(Long receiverId, Pageable pageable);

    Page<ReplyLetter> findAllByLetterId(Long letterId, Long receiverId, Pageable pageable);

    Optional<ReplyLetter> findById(Long replyLetterId);

    void delete(Long replyLetterId);

    void deleteByIds(List<Long> letterIds);
}
