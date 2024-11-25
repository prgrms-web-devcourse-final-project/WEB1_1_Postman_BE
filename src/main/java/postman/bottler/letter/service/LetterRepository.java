package postman.bottler.letter.service;

import postman.bottler.letter.domain.Letter;

public interface LetterRepository {
    Letter save(Letter letter);
}
