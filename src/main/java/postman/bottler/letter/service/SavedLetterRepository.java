package postman.bottler.letter.service;

import postman.bottler.letter.domain.SavedLetter;

public interface SavedLetterRepository {
    void save(SavedLetter savedLetter);

    boolean isAlreadySaved(Long userId, Long letterId);

    void remove(Long userId, Long letterId);

    boolean existsById(Long userId, Long letterId);
}
