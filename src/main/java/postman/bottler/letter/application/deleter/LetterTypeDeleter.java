package postman.bottler.letter.application.deleter;


import java.util.List;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;

public enum LetterTypeDeleter {

    LETTER {
        @Override
        public void delete(BoxType boxType, List<Long> ids, Long userId, LetterDeletionContext context) {
            switch (boxType) {
                case SEND -> deleteSendLetters(ids, userId, context);
                case RECEIVE -> deleteReceiveLetters(ids, userId, context);
            }
        }

        private void deleteSendLetters(List<Long> ids, Long userId, LetterDeletionContext context) {
            context.letterService().softDeleteLetters(ids, userId);
            context.letterKeywordService().markKeywordsAsDeleted(ids);
            context.letterBoxService().deleteByLetterIdsAndType(ids, LetterType.LETTER, BoxType.NONE);
        }

        private void deleteReceiveLetters(List<Long> ids, Long userId, LetterDeletionContext context) {
            context.letterBoxService()
                    .deleteByLetterIdsAndTypeForUser(ids, LetterType.LETTER, BoxType.RECEIVE, userId);
        }
    },
    REPLY_LETTER {
        @Override
        public void delete(BoxType boxType, List<Long> ids, Long userId, LetterDeletionContext context) {
            switch (boxType) {
                case SEND -> deleteSendReplyLetters(ids, userId, context);
                case RECEIVE -> deleteReceiveReplyLetters(ids, userId, context);
            }
        }

        private void deleteSendReplyLetters(List<Long> ids, Long userId, LetterDeletionContext context) {
            context.replyLetterService().softDeleteReplyLetters(ids, userId);
            context.letterBoxService().deleteByLetterIdsAndType(ids, LetterType.REPLY_LETTER, BoxType.NONE);
        }

        private void deleteReceiveReplyLetters(List<Long> ids, Long userId, LetterDeletionContext context) {
            context.letterBoxService()
                    .deleteByLetterIdsAndTypeForUser(ids, LetterType.REPLY_LETTER, BoxType.RECEIVE, userId);
        }
    };

    public abstract void delete(BoxType boxType, List<Long> ids, Long userId, LetterDeletionContext context);
}
