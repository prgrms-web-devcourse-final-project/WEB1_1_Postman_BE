package postman.bottler.letter.application.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import postman.bottler.keyword.application.service.LetterKeywordService;
import postman.bottler.letter.application.dto.LetterDeleteDTO;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;

@ExtendWith(MockitoExtension.class)
class LetterDeletionServiceTest {

    @InjectMocks
    private LetterDeletionService letterDeletionService;

    @Mock
    private LetterService letterService;

    @Mock
    private ReplyLetterService replyLetterService;

    @Mock
    private LetterBoxService letterBoxService;

    @Mock
    private LetterKeywordService letterKeywordService;

    @Test
    @DisplayName("단일 편지 삭제")
    void deleteLetter() {
        // given
        LetterDeleteDTO letterDeleteDTO = new LetterDeleteDTO(1L, LetterType.LETTER, BoxType.SEND);
        Long userId = 100L;

        // when
        letterDeletionService.deleteLetter(letterDeleteDTO, userId);

        // then
        verify(letterBoxService, times(1))
                .deleteByLetterIdsAndType(eq(List.of(1L)), eq(LetterType.LETTER), eq(BoxType.NONE));
    }

    @Test
    @DisplayName("다중 편지 삭제")
    void deleteLetters() {
        // given
        List<LetterDeleteDTO> letterDeleteDTOS = List.of(
                new LetterDeleteDTO(1L, LetterType.LETTER, BoxType.SEND),
                new LetterDeleteDTO(2L, LetterType.REPLY_LETTER, BoxType.RECEIVE)
        );
        Long userId = 100L;

        // when
        letterDeletionService.deleteLetters(letterDeleteDTOS, userId);

        // then
        verify(letterBoxService, atLeastOnce())
                .deleteByLetterIdsAndType(eq(List.of(1L)), eq(LetterType.LETTER), eq(BoxType.NONE));
        verify(letterBoxService, atLeastOnce())
                .deleteByLetterIdsAndTypeForUser(eq(List.of(2L)), eq(LetterType.REPLY_LETTER), eq(BoxType.RECEIVE),
                        eq(userId));
    }

    @Test
    @DisplayName("그룹화된 요청 처리")
    void processGroupedRequests() {
        // given
        Long userId = 100L;

        // when
        letterDeletionService.deleteLetters(
                List.of(new LetterDeleteDTO(1L, LetterType.LETTER, BoxType.SEND)), userId
        );

        // then
        verify(letterBoxService, atLeastOnce())
                .deleteByLetterIdsAndType(eq(List.of(1L)), eq(LetterType.LETTER), eq(BoxType.NONE));
    }
}
