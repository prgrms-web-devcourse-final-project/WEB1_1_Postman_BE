package postman.bottler.mapletter.service;

import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.ReplyMapLetter;

@Repository
public interface ReplyMapLetterRepository {
    ReplyMapLetter save(ReplyMapLetter replyMapLetter);
}
