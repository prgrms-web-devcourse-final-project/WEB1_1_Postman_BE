package postman.bottler.letter.service;

import postman.bottler.letter.domain.SavedLetter;

public interface SavedLetterRepository {
    void save(SavedLetter savedLetter);

    boolean isAlreadySaved(long userId, Long letterId);
}
