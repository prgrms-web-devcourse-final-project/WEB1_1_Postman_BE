package postman.bottler.letter.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import postman.bottler.letter.domain.Letter;

public interface LetterRepository {
    Letter save(Letter letter);

    void remove(Long letterId);

    Optional<Letter> findById(Long letterId);

    boolean existsById(Long letterId);

    Page<Letter> findAll(Long userId, Pageable pageable);
}
