package postman.bottler.letter.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.dto.request.LetterDeleteRequestDTO;

@Service
@RequiredArgsConstructor
public class DeleteManagerService {
    private final LetterService letterService;
    private final ReplyLetterService replyLetterService;
    private final LetterBoxService letterBoxService;

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

        // LETTER 삭제
        if (groupedByTypeAndBox.containsKey(LetterType.LETTER)) {
            Map<BoxType, List<Long>> letterBoxMap = groupedByTypeAndBox.get(LetterType.LETTER);

            // SEND BoxType
            if (letterBoxMap.containsKey(BoxType.SEND)) {
                List<Long> letterIds = letterBoxMap.get(BoxType.SEND);
                letterService.deleteLetters(letterIds); // LetterService에서 삭제
                letterBoxService.deleteAllByType(letterIds, LetterType.LETTER);
            }

            // RECEIVE BoxType
            if (letterBoxMap.containsKey(BoxType.RECEIVE)) {
                List<Long> letterIds = letterBoxMap.get(BoxType.RECEIVE);
                letterBoxService.deleteByType(letterIds, LetterType.LETTER, BoxType.RECEIVE);
            }
        }

        // REPLY_LETTER 삭제
        if (groupedByTypeAndBox.containsKey(LetterType.REPLY_LETTER)) {
            Map<BoxType, List<Long>> replyLetterBoxMap = groupedByTypeAndBox.get(LetterType.REPLY_LETTER);

            // SEND BoxType
            if (replyLetterBoxMap.containsKey(BoxType.SEND)) {
                List<Long> replyLetterIds = replyLetterBoxMap.get(BoxType.SEND);
                replyLetterService.deleteReplyLetters(replyLetterIds); // ReplyLetterService에서 삭제
                letterBoxService.deleteAllByType(replyLetterIds, LetterType.REPLY_LETTER);
            }

            // RECEIVE BoxType
            if (replyLetterBoxMap.containsKey(BoxType.RECEIVE)) {
                List<Long> replyLetterIds = replyLetterBoxMap.get(BoxType.RECEIVE);
                letterBoxService.deleteByType(replyLetterIds, LetterType.REPLY_LETTER, BoxType.RECEIVE);
            }
        }
    }

    @Transactional
    public void deleteLetter(Long letterId) {
        letterService.deleteLetter(letterId);
        letterBoxService.deleteLetter(letterId);
    }

    @Transactional
    public void deleteReplyLetter(Long replyLetterId) {
        replyLetterService.deleteReplyLetter(replyLetterId);
        letterBoxService.deleteLetter(replyLetterId);
    }
}
