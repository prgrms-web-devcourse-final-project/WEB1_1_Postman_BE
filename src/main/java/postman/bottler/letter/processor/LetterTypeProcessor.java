package postman.bottler.letter.processor;


import java.util.List;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;

public enum LetterTypeProcessor {

    LETTER {
        @Override
        public void process(BoxType boxType, List<Long> ids, Long userId, LetterDeletionContext context) {
            switch (boxType) {
                case SEND -> processSendLetters(ids, userId, context);
                case RECEIVE -> processReceiveLetters(ids, userId, context);
            }
        }

        private void processSendLetters(List<Long> ids, Long userId, LetterDeletionContext context) {
            context.letterService().deleteLetters(ids, userId);
            context.letterKeywordService().markKeywordsAsDeleted(ids);
            context.letterBoxService().deleteByIdsAndType(ids, LetterType.LETTER, BoxType.UNKNOWN);
        }

        private void processReceiveLetters(List<Long> ids, Long userId, LetterDeletionContext context) {
            context.letterBoxService().deleteByIdsAndTypeAndUserId(ids, LetterType.LETTER, BoxType.RECEIVE, userId);
        }
    },
    REPLY_LETTER {
        @Override
        public void process(BoxType boxType, List<Long> ids, Long userId, LetterDeletionContext context) {
            switch (boxType) {
                case SEND -> processSendReplyLetters(ids, userId, context);
                case RECEIVE -> processReceiveReplyLetters(ids, userId, context);
            }
        }

        private void processSendReplyLetters(List<Long> ids, Long userId, LetterDeletionContext context) {
            context.replyLetterService().deleteReplyLetters(ids, userId);
            context.letterBoxService().deleteByIdsAndType(ids, LetterType.REPLY_LETTER, BoxType.UNKNOWN);
        }

        private void processReceiveReplyLetters(List<Long> ids, Long userId, LetterDeletionContext context) {
            context.letterBoxService()
                    .deleteByIdsAndTypeAndUserId(ids, LetterType.REPLY_LETTER, BoxType.RECEIVE, userId);
        }
    };

    public abstract void process(BoxType boxType, List<Long> ids, Long userId, LetterDeletionContext context);
}