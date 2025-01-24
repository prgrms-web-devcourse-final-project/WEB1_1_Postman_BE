package postman.bottler.letter.application.repository;

import java.util.List;
import java.util.Optional;
import postman.bottler.letter.domain.Letter;

public interface LetterRepository {
    Letter save(Letter letter);

    Optional<Letter> findById(Long letterId);

    List<Letter> findAllByIds(List<Long> letterIds);

    List<Letter> findAllByUserId(Long userId);

    void softDeleteByIds(List<Long> letterIds);

    void softBlockById(Long letterId);

    boolean existsById(Long letterId);
}
