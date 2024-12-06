package postman.bottler.letter.service;

import java.util.List;
import java.util.Optional;
import postman.bottler.letter.domain.Letter;

public interface LetterRepository {
    Letter save(Letter letter);

    Optional<Letter> findById(Long letterId);

    void deleteByIds(List<Long> letterIds);

    void blockLetterById(Long letterId);

    List<Letter> findAllByIds(List<Long> letterIds);
}
