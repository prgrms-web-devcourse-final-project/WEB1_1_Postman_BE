package postman.bottler.letter.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import postman.bottler.TestBase;
import postman.bottler.letter.application.dto.LetterBoxDTO;
import postman.bottler.letter.application.dto.request.PageRequestDTO;
import postman.bottler.letter.application.dto.response.LetterSummaryResponseDTO;
import postman.bottler.letter.application.repository.LetterBoxRepository;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterBox;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.exception.InvalidInputException;
import postman.bottler.letter.exception.UnauthorizedLetterAccessException;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class LetterBoxServiceTest extends TestBase {

    @InjectMocks
    private LetterBoxService letterBoxService;

    @Mock
    private LetterBoxRepository letterBoxRepository;

    private PageRequestDTO pageRequestDTO;
    private Page<LetterSummaryResponseDTO> mockPage;

    @BeforeEach
    void setUp() {
        pageRequestDTO = new PageRequestDTO(1, 10, "createdAt");

        mockPage = new PageImpl<>(
                List.of(
                        new LetterSummaryResponseDTO(101L, "Title1", "Label1", LetterType.LETTER, BoxType.RECEIVE,
                                LocalDateTime.now()),
                        new LetterSummaryResponseDTO(102L, "Title2", "Label2", LetterType.LETTER, BoxType.RECEIVE,
                                LocalDateTime.now())
                )
        );
    }

    @Nested
    @DisplayName("편지 저장")
    class SaveLetterTests {

        @Test
        @DisplayName("정상적으로 편지를 저장한다")
        void saveLetter() {
            // given
            LetterBoxDTO letterBoxDTO = LetterBoxDTO.of(1L, 101L, LetterType.LETTER, BoxType.RECEIVE,
                    LocalDateTime.now());

            // when
            letterBoxService.saveLetter(letterBoxDTO);

            // then
            ArgumentCaptor<LetterBox> captor = ArgumentCaptor.forClass(LetterBox.class);
            verify(letterBoxRepository, times(1)).save(captor.capture());

            LetterBox captured = captor.getValue();
            assertThat(captured.getUserId()).isEqualTo(letterBoxDTO.userId());
            assertThat(captured.getLetterId()).isEqualTo(letterBoxDTO.letterId());
        }

        @Test
        @DisplayName("편지 저장 실패 시 InvalidInputException 발생")
        void saveLetterWithNullValues() {
            // given
            LetterBoxDTO invalidLetterBoxDTO = LetterBoxDTO.of(null, null, null, null, null);

            // then
            assertThatThrownBy(() -> letterBoxService.saveLetter(invalidLetterBoxDTO))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("LetterBoxDTO 또는 필수 값이 null입니다.");
        }
    }

    @Nested
    @DisplayName("편지 요약 조회")
    class FindLetterSummariesTests {

        @Test
        @DisplayName("모든 편지 요약을 성공적으로 조회한다")
        void findAllLetterSummaries() {
            // given
            when(letterBoxRepository.findAllLetters(1L, pageRequestDTO.toPageable())).thenReturn(mockPage);

            // when
            Page<LetterSummaryResponseDTO> result = letterBoxService.findAllLetterSummaries(pageRequestDTO, 1L);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
        }

        @Test
        @DisplayName("받은 편지 요약 조회 시 빈 결과 처리")
        void findReceivedLetterSummariesWithNoData() {
            // given
            when(letterBoxRepository.findReceivedLetters(1L, pageRequestDTO.toPageable())).thenReturn(Page.empty());

            // when
            Page<LetterSummaryResponseDTO> result = letterBoxService.findReceivedLetterSummaries(pageRequestDTO, 1L);

            // then
            assertThat(result.getContent()).isEmpty();
        }
    }

    @Nested
    @DisplayName("사용자 받은 편지 조회")
    class FindReceivedLettersTests {

        @Test
        @DisplayName("사용자가 받은 편지 ID를 성공적으로 조회한다")
        void findReceivedLettersByUserId() {
            // given
            when(letterBoxRepository.findReceivedLettersByUserId(1L)).thenReturn(List.of(101L, 102L));

            // when
            List<Long> result = letterBoxService.findReceivedLettersByUserId(1L);

            // then
            assertThat(result).containsExactly(101L, 102L);
        }

        @Test
        @DisplayName("존재하지 않는 사용자 ID로 편지 요약 조회 시 InvalidInputException 발생")
        void findReceivedLettersByInvalidUserId() {
            // given
            Long invalidUserId = 0L;

            // then
            assertThatThrownBy(() -> letterBoxService.findAllLetterSummaries(pageRequestDTO, invalidUserId))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("유효하지 않은 userId");
        }
    }

    @Nested
    @DisplayName("편지 삭제")
    class DeleteLetterTests {

        @Test
        @DisplayName("편지 삭제 시 빈 letterIds 전달 예외 처리")
        void deleteByLetterIdsAndTypeWithEmptyList() {
            // then
            assertThatThrownBy(
                    () -> letterBoxService.deleteByLetterIdsAndType(Collections.emptyList(), LetterType.LETTER,
                            BoxType.RECEIVE))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("삭제 작업에 필요한 값이 누락되었습니다.");
        }

        @Test
        @DisplayName("편지 삭제 시 null letterType 전달 예외 처리")
        void deleteByLetterIdsAndTypeWithNullLetterType() {
            // given
            List<Long> validLetterIds = List.of(101L, 102L);

            // then
            assertThatThrownBy(() -> letterBoxService.deleteByLetterIdsAndType(validLetterIds, null, BoxType.RECEIVE))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("삭제 작업에 필요한 값이 누락되었습니다.");
        }

        @Test
        @DisplayName("사용자 편지 삭제 시 userId가 0일 경우 예외 발생")
        void deleteByLetterIdsAndTypeForUserWithZeroUserId() {
            // given
            List<Long> validLetterIds = List.of(101L);

            // then
            assertThatThrownBy(() -> letterBoxService.deleteByLetterIdsAndTypeForUser(validLetterIds, LetterType.LETTER,
                    BoxType.RECEIVE, 0L))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("삭제 작업에 필요한 값이 누락되었거나 유효하지 않습니다.");
        }
    }

    @Test
    @DisplayName("사용자 권한 없는 편지 접근 시 UnauthorizedLetterAccessException 발생")
    void validateLetterInUserBoxUnauthorizedAccess() {
        // given
        when(letterBoxRepository.existsByLetterIdAndUserId(101L, 1L)).thenReturn(false);

        // then
        assertThatThrownBy(() -> letterBoxService.validateLetterInUserBox(101L, 1L))
                .isInstanceOf(UnauthorizedLetterAccessException.class)
                .hasMessage("사용자가 해당 편지에 접근할 권한이 없습니다.");
    }
}
