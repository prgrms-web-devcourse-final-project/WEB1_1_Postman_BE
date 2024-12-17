package postman.bottler.letter.service;

import java.util.List;
import java.util.Optional;
import postman.bottler.letter.domain.Letter;

public interface LetterRepository {
    Letter save(Letter letter);

    Optional<Letter> findById(Long letterId);

    List<Letter> findAllByIds(List<Long> letterIds);

    List<Letter> findAllByUserId(Long userId);

    void deleteByIds(List<Long> letterIds);

    void blockLetterById(Long letterId);

    boolean checkLetterExists(Long letterId);
}
