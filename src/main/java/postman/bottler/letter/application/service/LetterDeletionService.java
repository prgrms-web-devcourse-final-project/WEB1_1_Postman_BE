package postman.bottler.letter.application.service;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.keyword.application.service.LetterKeywordService;
import postman.bottler.letter.application.dto.LetterDeleteDTO;
import postman.bottler.letter.application.dto.LetterDeleteRequests;
import postman.bottler.letter.application.service.deleter.BoxTypeDeleter;
import postman.bottler.letter.application.service.deleter.LetterDeletionContext;
import postman.bottler.letter.application.service.deleter.LetterTypeDeleter;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;

@Service
@RequiredArgsConstructor
@Slf4j
public class LetterDeletionService {

    private final LetterService letterService;
    private final ReplyLetterService replyLetterService;
    private final LetterBoxService letterBoxService;
    private final LetterKeywordService letterKeywordService;

    @Transactional
    public void deleteLetter(LetterDeleteDTO letterDeleteDTO, Long userId) {
        deleteLetters(List.of(letterDeleteDTO), userId);
    }

    @Transactional
    public void deleteLetters(List<LetterDeleteDTO> letterDeleteDTOS, Long userId) {
        log.info("편지 삭제 요청: userId={}, 요청 개수={}", userId, letterDeleteDTOS.size());

        Map<LetterType, Map<BoxType, List<Long>>> groupedRequests = groupRequestsByTypeAndBox(letterDeleteDTOS);

        log.debug("편지 삭제 요청 그룹화 완료: userId={}, 그룹화된 개수={}", userId, groupedRequests.size());

        processGroupedRequests(groupedRequests, userId);

        log.info("편지 삭제 완료: userId={}, 삭제된 편지 개수={}", userId, letterDeleteDTOS.size());
    }

    @Transactional
    public void deleteAllSavedLetters(Long userId) {
        executeBoxTypeDeletion(BoxTypeDeleter.NONE, userId);
    }

    @Transactional
    public void deleteAllSavedReceivedLetters(Long userId) {
        executeBoxTypeDeletion(BoxTypeDeleter.RECEIVE, userId);
    }

    @Transactional
    public void deleteAllSavedSentLetters(Long userId) {
        executeBoxTypeDeletion(BoxTypeDeleter.SEND, userId);
    }

    private Map<LetterType, Map<BoxType, List<Long>>> groupRequestsByTypeAndBox(
            List<LetterDeleteDTO> letterDeleteDTOS) {
        return new LetterDeleteRequests(letterDeleteDTOS).groupByTypeAndBox();
    }

    private void processGroupedRequests(Map<LetterType, Map<BoxType, List<Long>>> groupedRequests, Long userId) {
        groupedRequests.forEach((letterType, boxTypeMap) -> processLetterType(userId, letterType, boxTypeMap));
    }

    private void processLetterType(Long userId, LetterType letterType, Map<BoxType, List<Long>> boxTypeMap) {
        LetterTypeDeleter processor = LetterTypeDeleter.valueOf(letterType.name());
        LetterDeletionContext letterDeletionContext = createLetterDeletionContext();

        boxTypeMap.forEach((boxType, ids) -> {
            log.debug("보관 타입별 삭제 처리: userId={}, 보관 타입={}, 편지 타입={}, 삭제 개수={}", userId, boxType, letterType, ids.size());
            processor.delete(boxType, ids, userId, letterDeletionContext);
        });
    }

    private void executeBoxTypeDeletion(BoxTypeDeleter deleter, Long userId) {
        LetterDeletionContext context = createLetterDeletionContext();
        deleter.delete(userId, context);
    }

    private LetterDeletionContext createLetterDeletionContext() {
        return new LetterDeletionContext(letterService, replyLetterService, letterBoxService, letterKeywordService);
    }
}
