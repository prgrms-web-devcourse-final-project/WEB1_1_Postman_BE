package postman.bottler.letter.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import postman.bottler.letter.domain.Letter;

public interface LetterRepository {
    Letter save(Letter letter);

    void delete(Long letterId);

    Optional<Letter> findById(Long letterId);

    boolean existsById(Long letterId);

    Page<Letter> findAll(Long userId, Pageable pageable);

    boolean existsByUserIdAndLetterId(Long userId, Long letterId);

    void deleteByIds(List<Long> letterIds);
}
