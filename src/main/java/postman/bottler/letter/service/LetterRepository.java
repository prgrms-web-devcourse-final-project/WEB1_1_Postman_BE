package postman.bottler.letter.service;

import java.util.Optional;
import postman.bottler.letter.domain.Letter;

public interface LetterRepository {
    Letter save(Letter letter);

    void remove(Long letterId);

    Optional<Letter> findById(Long letterId);

    boolean existsById(Long letterId);
}
