package postman.bottler.replyletter.controller;

import org.springframework.data.domain.Pageable;
import postman.bottler.replyletter.domain.ReplyLetter;
import postman.bottler.replyletter.dto.request.ReplyLetterRequestDTO;

import java.util.List;

public interface ReplyLetterService {
    //편지 블락 처리 로직 편지 블락 시키고 3번 되면 정지요청만 보내기
    void incrementWarningCount(Long letterId);
    ReplyLetter createReply(Long letterId, ReplyLetterRequestDTO request);
    List<ReplyLetter> getReplies(Long letterId, Pageable pageable);
}
