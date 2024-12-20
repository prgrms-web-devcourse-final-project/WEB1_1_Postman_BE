package postman.bottler.letter.processor;

import postman.bottler.keyword.service.LetterKeywordService;
import postman.bottler.letter.service.LetterBoxService;
import postman.bottler.letter.service.LetterService;
import postman.bottler.letter.service.ReplyLetterService;

public record LetterDeletionContext(
        LetterService letterService,
        ReplyLetterService replyLetterService,
        LetterBoxService letterBoxService,
        LetterKeywordService letterKeywordService
) {
}