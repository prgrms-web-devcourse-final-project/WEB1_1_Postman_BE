package postman.bottler.letter.controller;

import org.springframework.data.domain.Pageable;
import postman.bottler.letter.domain.ReplyLetter;
import postman.bottler.letter.dto.request.ReplyLetterRequestDTO;

import java.util.List;

public interface ReplyLetterService {
    ReplyLetter createReply(Long letterId, ReplyLetterRequestDTO request);
    List<ReplyLetter> getReplies(Long letterId, Pageable pageable);
}
