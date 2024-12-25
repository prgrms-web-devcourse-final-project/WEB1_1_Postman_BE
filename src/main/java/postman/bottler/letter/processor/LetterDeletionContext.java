package postman.bottler.letter.processor;

import postman.bottler.keyword.applications.service.LetterKeywordService;
import postman.bottler.letter.applications.service.LetterBoxService;
import postman.bottler.letter.applications.service.LetterService;
import postman.bottler.letter.applications.service.ReplyLetterService;

public record LetterDeletionContext(
        LetterService letterService,
        ReplyLetterService replyLetterService,
        LetterBoxService letterBoxService,
        LetterKeywordService letterKeywordService
) {
}