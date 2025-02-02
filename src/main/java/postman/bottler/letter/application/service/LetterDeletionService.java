package postman.bottler.letter.application.service;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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
        Map<LetterType, Map<BoxType, List<Long>>> groupedRequests = groupRequestsByTypeAndBox(letterDeleteDTOS);
        processGroupedRequests(groupedRequests, userId);
    }

    private Map<LetterType, Map<BoxType, List<Long>>> groupRequestsByTypeAndBox(
            List<LetterDeleteDTO> letterDeleteDTOS) {
        return new LetterDeleteRequests(letterDeleteDTOS).groupByTypeAndBox();
    }

    private void processGroupedRequests(Map<LetterType, Map<BoxType, List<Long>>> groupedRequests, Long userId) {
        groupedRequests.forEach(
                (letterType, boxTypeMap) ->
                        processLetterType(userId, letterType, boxTypeMap)
        );
    }

    private void processLetterType(
            Long userId, LetterType letterType, Map<BoxType, List<Long>> boxTypeMap
    ) {
        LetterTypeProcessor processor = LetterTypeProcessor.valueOf(letterType.name());
        boxTypeMap.forEach((boxType, ids) -> processor.process(boxType, ids, userId, createLetterDeletionContext()));
    }

    private LetterDeletionContext createLetterDeletionContext() {
        return new LetterDeletionContext(
                letterService, replyLetterService, letterBoxService, letterKeywordService
        );
    }
}
