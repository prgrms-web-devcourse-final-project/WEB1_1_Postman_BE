package postman.bottler.letter.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import postman.bottler.TestBase;
import postman.bottler.letter.application.dto.response.LetterSummaryResponseDTO;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterBox;
import postman.bottler.letter.domain.LetterType;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class LetterBoxRepositoryImplTest extends TestBase {

    @InjectMocks
    private LetterBoxRepositoryImpl repository;

    @Mock
    private LetterBoxQueryRepository queryRepository;

    @Mock
    private LetterBoxJdbcRepository jdbcRepository;

    private LetterSummaryResponseDTO summaryResponseDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        LetterBox letterBox = LetterBox.builder()
                .userId(1L)
                .letterId(101L)
                .letterType(LetterType.LETTER)
                .boxType(BoxType.RECEIVE)
                .createdAt(LocalDateTime.now())
                .build();

        summaryResponseDTO = new LetterSummaryResponseDTO(
                101L,
                "Test Title",
                "Test Label",
                LetterType.LETTER,
                BoxType.SEND,
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("사용자의 모든 편지를 페이징 처리로 조회")
    void findAllLettersForUser() {
        // given
        when(queryRepository.fetchLetters(1L, null, pageable)).thenReturn(List.of(summaryResponseDTO));
        when(queryRepository.countLetters(1L, null)).thenReturn(1L);

        // when
        Page<LetterSummaryResponseDTO> result = repository.findLetters(1L, pageable, BoxType.UNKNOWN);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.getTotalElements()).isEqualTo(1L);
    }

    @Test
    @DisplayName("사용자가 보낸 편지를 조회")
    void findSentLettersForUser() {
        // given
        when(queryRepository.fetchLetters(1L, BoxType.SEND, pageable)).thenReturn(List.of(summaryResponseDTO));
        when(queryRepository.countLetters(1L, BoxType.SEND)).thenReturn(1L);

        // when
        Page<LetterSummaryResponseDTO> result = repository.findLetters(1L, pageable, BoxType.SEND);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.getContent().get(0).boxType()).isEqualTo(BoxType.SEND);
    }

    @Test
    @DisplayName("사용자가 받은 편지를 조회")
    void findReceivedLettersForUser() {
        // given
        summaryResponseDTO = new LetterSummaryResponseDTO(
                101L,
                "Test Title",
                "Test Label",
                LetterType.LETTER,
                BoxType.RECEIVE,
                LocalDateTime.now()
        );
        when(queryRepository.fetchLetters(1L, BoxType.RECEIVE, pageable)).thenReturn(List.of(summaryResponseDTO));
        when(queryRepository.countLetters(1L, BoxType.RECEIVE)).thenReturn(1L);

        // when
        Page<LetterSummaryResponseDTO> result = repository.findLetters(1L, pageable, BoxType.RECEIVE);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.getContent().get(0).boxType()).isEqualTo(BoxType.RECEIVE);
    }

    @Test
    @DisplayName("사용자가 받은 편지 ID 목록을 반환")
    void returnReceivedLetterIdsForUser() {
        // given
        when(queryRepository.findReceivedLetterIdsByUserId(1L)).thenReturn(List.of(101L));

        // when
        List<Long> result = repository.findReceivedLetterIdsByUserId(1L);

        // then
        assertThat(result).containsExactly(101L);
    }

    @Test
    @DisplayName("조건에 맞는 편지를 삭제")
    void deleteLettersByCondition() {
        // when
        repository.deleteByCondition(List.of(101L), LetterType.LETTER, BoxType.RECEIVE);

        // then
        verify(queryRepository, times(1))
                .deleteByCondition(List.of(101L), LetterType.LETTER, BoxType.RECEIVE);
    }

    @Test
    @DisplayName("특정 사용자와 조건에 맞는 편지를 삭제")
    void deleteLettersByConditionAndUserId() {
        // when
        repository.deleteByConditionAndUserId(List.of(101L), LetterType.LETTER, BoxType.RECEIVE, 1L);

        // then
        verify(queryRepository, times(1))
                .deleteByConditionAndUserId(List.of(101L), LetterType.LETTER, BoxType.RECEIVE, 1L);
    }

    @Test
    @DisplayName("특정 사용자와 편지의 존재 여부를 확인")
    void checkIfLetterExistsForUser() {
        // given
        when(jdbcRepository.existsByUserIdAndLetterId(101L, 1L)).thenReturn(true);

        // when
        boolean exists = repository.existsByLetterIdAndUserId(101L, 1L);

        // then
        assertThat(exists).isTrue();
    }
}
