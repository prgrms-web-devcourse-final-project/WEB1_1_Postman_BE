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
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.processor.LetterDeletionContext;
import postman.bottler.letter.processor.LetterTypeProcessor;

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
        log.debug("단일 편지 삭제 요청: userId={}, letterId={}, 편지 타입={}, 보관 타입={}", userId, letterDeleteDTO.letterId(),
                letterDeleteDTO.letterType(), letterDeleteDTO.boxType());

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

    private Map<LetterType, Map<BoxType, List<Long>>> groupRequestsByTypeAndBox(
            List<LetterDeleteDTO> letterDeleteDTOS) {
        log.debug("편지 삭제 요청을 타입별로 그룹화 중: 요청 개수={}", letterDeleteDTOS.size());
        return new LetterDeleteRequests(letterDeleteDTOS).groupByTypeAndBox();
    }

    private void processGroupedRequests(Map<LetterType, Map<BoxType, List<Long>>> groupedRequests, Long userId) {
        log.debug("그룹화된 편지 삭제 처리 시작: userId={}, 편지 타입={}", userId, groupedRequests.keySet());

        groupedRequests.forEach((letterType, boxTypeMap) -> processLetterType(userId, letterType, boxTypeMap));

        log.debug("그룹화된 편지 삭제 처리 완료: userId={}", userId);
    }

    private void processLetterType(Long userId, LetterType letterType, Map<BoxType, List<Long>> boxTypeMap) {
        log.debug("편지 타입별 삭제 처리 시작: userId={}, 편지 타입={}", userId, letterType);

        LetterTypeProcessor processor = LetterTypeProcessor.valueOf(letterType.name());
        LetterDeletionContext letterDeletionContext = createLetterDeletionContext();

        boxTypeMap.forEach((boxType, ids) -> {
            log.debug("보관 타입별 삭제 처리: userId={}, 보관 타입={}, 편지 타입={}, 삭제 개수={}", userId, boxType, letterType, ids.size());
            processor.process(boxType, ids, userId, letterDeletionContext);
        });
    }

    private LetterDeletionContext createLetterDeletionContext() {
        return new LetterDeletionContext(letterService, replyLetterService, letterBoxService, letterKeywordService);
    }
}
