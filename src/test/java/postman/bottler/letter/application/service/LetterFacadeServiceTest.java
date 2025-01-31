package postman.bottler.letter.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import postman.bottler.TestBase;
import postman.bottler.keyword.application.service.LetterKeywordService;
import postman.bottler.keyword.application.service.RedisLetterService;
import postman.bottler.keyword.domain.LetterKeyword;
import postman.bottler.letter.application.dto.request.LetterRequestDTO;
import postman.bottler.letter.application.dto.response.LetterDetailResponseDTO;
import postman.bottler.letter.application.dto.response.LetterRecommendSummaryResponseDTO;
import postman.bottler.letter.application.dto.response.LetterResponseDTO;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.exception.UnauthorizedLetterAccessException;
import postman.bottler.user.application.service.UserService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class LetterFacadeServiceTest extends TestBase {

    @InjectMocks
    private LetterFacadeService letterFacadeService;

    @Mock
    private LetterBoxService letterBoxService;

    @Mock
    private LetterService letterService;

    @Mock
    private LetterKeywordService letterKeywordService;

    @Mock
    private RedisLetterService redisLetterService;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("새로운 편지를 생성한다.")
    void createLetter() {
        // given
        LetterRequestDTO requestDTO = new LetterRequestDTO(
                "테스트 제목", "테스트 내용", List.of("키워드1", "키워드2"),
                "폰트", "편지지", "라벨");
        Long userId = 1L;

        Letter mockLetter = Letter.builder()
                .id(10L).title("테스트 제목").content("테스트 내용").build();
        List<LetterKeyword> mockKeywords = List.of(
                LetterKeyword.from(10L, "키워드1"),
                LetterKeyword.from(10L, "키워드2")
        );

        when(letterService.createLetter(any(), eq(userId))).thenReturn(mockLetter);
        when(letterKeywordService.createLetterKeywords(eq(10L), anyList())).thenReturn(mockKeywords);

        // when
        LetterResponseDTO result = letterFacadeService.createLetter(requestDTO, userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.letterId()).isEqualTo(10L);
        verify(letterService, times(1)).createLetter(any(), eq(userId));
        verify(letterKeywordService, times(1)).createLetterKeywords(eq(10L), anyList());
    }

    @Nested
    @DisplayName("편지 상세 조회")
    class FindLetterDetailTests {

        @Test
        @DisplayName("편지 상세 정보를 조회한다.")
        void findLetterDetail() {
            // given
            Long letterId = 10L;
            Long currentUserId = 1L;

            Letter mockLetter = Letter.builder()
                    .id(10L)
                    .userId(1L)
                    .title("테스트 제목")
                    .content("테스트 내용")
                    .build();

            List<LetterKeyword> mockKeywords = List.of(
                    LetterKeyword.from(10L, "키워드1"),
                    LetterKeyword.from(10L, "키워드2")
            );

            when(letterKeywordService.getKeywords(eq(letterId))).thenReturn(mockKeywords);
            when(userService.getProfileImageUrlById(eq(currentUserId))).thenReturn("profileUrl");
            when(letterService.findLetter(eq(letterId))).thenReturn(mockLetter);

            doNothing().when(letterBoxService).validateLetterInUserBox(eq(letterId), eq(currentUserId));

            // when
            LetterDetailResponseDTO result = letterFacadeService.findLetterDetail(letterId, currentUserId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.letterId()).isEqualTo(10L);
            assertThat(result.isOwner()).isTrue();
            verify(letterBoxService, times(1)).validateLetterInUserBox(eq(letterId), eq(currentUserId));
            verify(letterKeywordService, times(1)).getKeywords(eq(letterId));
            verify(userService, times(1)).getProfileImageUrlById(eq(currentUserId));
            verify(letterService, times(1)).findLetter(eq(letterId));
        }

        @Test
        @DisplayName("권한이 없는 경우 예외 발생")
        void findLetterDetailUnauthorized() {
            // given
            Long letterId = 10L;
            Long currentUserId = 1L;

            doThrow(new UnauthorizedLetterAccessException("권한이 없습니다."))
                    .when(letterBoxService)
                    .validateLetterInUserBox(eq(letterId), eq(currentUserId));

            // when & then
            assertThrows(UnauthorizedLetterAccessException.class,
                    () -> letterFacadeService.findLetterDetail(letterId, currentUserId));

            verify(letterBoxService, times(1)).validateLetterInUserBox(eq(letterId), eq(currentUserId));
        }
    }

    @Test
    @DisplayName("추천 편지 목록을 조회한다.")
    void findRecommendHeaders() {
        // given
        Long userId = 1L;
        List<Long> mockLetterIds = List.of(10L, 20L);
        List<Letter> mockLetters = List.of(
                Letter.builder().id(10L).title("제목1").content("내용1").build(),
                Letter.builder().id(20L).title("제목2").content("내용2").build()
        );

        when(redisLetterService.getRecommendations(eq(userId))).thenReturn(mockLetterIds);
        when(letterService.findRecommendedLetters(eq(mockLetterIds))).thenReturn(mockLetters);

        // when
        List<LetterRecommendSummaryResponseDTO> result = letterFacadeService.findRecommendHeaders(userId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).letterId()).isEqualTo(10L);
        verify(redisLetterService, times(1)).getRecommendations(eq(userId));
        verify(letterService, times(1)).findRecommendedLetters(eq(mockLetterIds));
    }
}
