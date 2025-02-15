package postman.bottler.letter.deleter;

import java.util.List;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;

public enum BoxTypeDeleter {
    SEND {
        @Override
        public void delete(Long userId, LetterDeletionContext context) {
            List<Long> letterIds = context.letterService().findIdsByUserId(userId);
            List<Long> replyLetterIds = context.replyLetterService().findIdsBySenderId(userId);

            deleteLetterBox(context, letterIds, replyLetterIds);

            deleteLetters(userId, context, letterIds, replyLetterIds);
        }

        private void deleteLetterBox(LetterDeletionContext context, List<Long> letterIds, List<Long> replyLetterIds) {
            if (!letterIds.isEmpty()) {
                context.letterBoxService().deleteByLetterIdsAndType(letterIds, LetterType.LETTER, BoxType.NONE);
            }
            if (!replyLetterIds.isEmpty()) {
                context.letterBoxService()
                        .deleteByLetterIdsAndType(replyLetterIds, LetterType.REPLY_LETTER, BoxType.NONE);
            }
        }

        private void deleteLetters(Long userId, LetterDeletionContext context, List<Long> letterIds,
                                   List<Long> replyLetterIds) {
            if (!letterIds.isEmpty()) {
                context.letterService().softDeleteLetters(letterIds, userId);
                context.letterKeywordService().markKeywordsAsDeleted(letterIds);
            }
            if (!replyLetterIds.isEmpty()) {
                context.replyLetterService().softDeleteReplyLetters(replyLetterIds, userId);
            }
        }
    }, RECEIVE {
        @Override
        public void delete(Long userId, LetterDeletionContext context) {
            context.letterBoxService().deleteAllByBoxTypeForUser(userId, BoxType.RECEIVE);
        }
    }, NONE {
        @Override
        public void delete(Long userId, LetterDeletionContext context) {
            SEND.delete(userId, context);
            RECEIVE.delete(userId, context);
        }
    };

    public abstract void delete(Long userId, LetterDeletionContext context);
}
