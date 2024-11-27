package postman.bottler.letter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import postman.bottler.letter.domain.ReplyLetter;

public interface ReplyLetterRepository {
    ReplyLetter save(ReplyLetter replyLetter);

    Page<ReplyLetter> findAll(Long userId, Pageable pageable);

    Page<ReplyLetter> findAllByLetterId(Long letterId, Long userId, Pageable pageable);
}
