package postman.bottler.letter.application.deleter;

import postman.bottler.keyword.application.service.LetterKeywordService;
import postman.bottler.letter.application.service.LetterBoxService;
import postman.bottler.letter.application.service.LetterService;
import postman.bottler.letter.application.service.ReplyLetterService;

public record LetterDeletionContext(
        LetterService letterService,
        ReplyLetterService replyLetterService,
        LetterBoxService letterBoxService,
        LetterKeywordService letterKeywordService
) {
}