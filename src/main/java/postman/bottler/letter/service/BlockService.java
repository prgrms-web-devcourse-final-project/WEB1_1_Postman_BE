package postman.bottler.letter.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import postman.bottler.keyword.service.LetterKeywordService;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.exception.InvalidLetterTypeException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockService {

    private final ReplyLetterService replyLetterService;
    private final LetterService letterService;
    private final LetterKeywordService letterKeywordService;

    public Long blockKeywordLetter(Long letterId, LetterType letterType) {
        Long userId = 0L;

        switch (letterType) {
            case LETTER -> {
                userId = letterService.blockLetter(letterId);
                letterKeywordService.markKeywordsAsDeleted(List.of(letterId));
            }
            case REPLY_LETTER -> userId = replyLetterService.blockLetter(letterId);
            default -> throw new InvalidLetterTypeException("유효하지 않은 타입입니다.");
        }

        return userId;
    }
}
