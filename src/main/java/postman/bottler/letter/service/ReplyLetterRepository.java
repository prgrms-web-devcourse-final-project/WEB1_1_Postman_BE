package postman.bottler.letter.service;

import postman.bottler.letter.domain.ReplyLetter;

public interface ReplyLetterRepository {
    ReplyLetter save(ReplyLetter domain);
}
