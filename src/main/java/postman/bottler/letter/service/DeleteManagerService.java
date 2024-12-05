package postman.bottler.letter.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.keyword.service.LetterKeywordService;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.dto.request.LetterDeleteRequestDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteManagerService {
    private final LetterService letterService;
    private final ReplyLetterService replyLetterService;
    private final LetterBoxService letterBoxService;
    private final LetterKeywordService letterKeywordService;

    @Transactional
    public void deleteLetters(List<LetterDeleteRequestDTO> letterDeleteRequestDTOs) {
        Map<LetterType, Map<BoxType, List<Long>>> groupedByTypeAndBox = letterDeleteRequestDTOs.stream()
                .collect(Collectors.groupingBy(
                        LetterDeleteRequestDTO::letterType,
                        Collectors.groupingBy(
                                LetterDeleteRequestDTO::boxType,
                                Collectors.mapping(LetterDeleteRequestDTO::letterId, Collectors.toList())
                        )
                ));

        if (groupedByTypeAndBox.containsKey(LetterType.LETTER)) {
            Map<BoxType, List<Long>> letterBoxMap = groupedByTypeAndBox.get(LetterType.LETTER);
            if (letterBoxMap.containsKey(BoxType.SEND)) {
                List<Long> letterIds = letterBoxMap.get(BoxType.SEND);
                letterService.deleteLetters(letterIds); // LetterService에서 삭제
                letterKeywordService.markKeywordsAsDeleted(letterIds);
                letterBoxService.deleteByIdsAndType(letterIds, LetterType.LETTER, BoxType.UNKNOWN);
            }
            if (letterBoxMap.containsKey(BoxType.RECEIVE)) {
                List<Long> letterIds = letterBoxMap.get(BoxType.RECEIVE);
                letterBoxService.deleteByIdsAndType(letterIds, LetterType.LETTER, BoxType.RECEIVE);
            }
        }

        if (groupedByTypeAndBox.containsKey(LetterType.REPLY_LETTER)) {
            Map<BoxType, List<Long>> replyLetterBoxMap = groupedByTypeAndBox.get(LetterType.REPLY_LETTER);
            if (replyLetterBoxMap.containsKey(BoxType.SEND)) {
                List<Long> replyLetterIds = replyLetterBoxMap.get(BoxType.SEND);
                replyLetterService.deleteReplyLetters(replyLetterIds); // ReplyLetterService에서 삭제
                letterBoxService.deleteByIdsAndType(replyLetterIds, LetterType.REPLY_LETTER, BoxType.UNKNOWN);
            }
            if (replyLetterBoxMap.containsKey(BoxType.RECEIVE)) {
                List<Long> replyLetterIds = replyLetterBoxMap.get(BoxType.RECEIVE);
                letterBoxService.deleteByIdsAndType(replyLetterIds, LetterType.REPLY_LETTER, BoxType.RECEIVE);
            }
        }
    }
}
