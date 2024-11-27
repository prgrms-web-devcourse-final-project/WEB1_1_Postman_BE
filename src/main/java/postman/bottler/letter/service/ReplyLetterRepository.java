package postman.bottler.letter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import postman.bottler.letter.domain.ReplyLetter;

public interface ReplyLetterRepository {
    ReplyLetter save(ReplyLetter domain);

    Page<ReplyLetter> findAll(Long userId, Pageable pageable);
}
