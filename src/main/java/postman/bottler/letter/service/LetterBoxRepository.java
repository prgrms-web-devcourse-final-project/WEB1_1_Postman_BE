package postman.bottler.letter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.domain.LetterBox;

public interface LetterBoxRepository {
    void save(LetterBox letterBox);

    boolean isSaved(Long userId, Long letterId);

    void remove(Long userId, Long letterId);

    Page<Letter> findSavedLetters(Long userId, Pageable pageable);
}
