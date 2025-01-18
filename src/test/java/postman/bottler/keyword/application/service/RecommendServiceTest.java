package postman.bottler.keyword.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import postman.bottler.TestBase;
import postman.bottler.keyword.application.repository.LetterKeywordRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class RecommendServiceTest extends TestBase {

    @InjectMocks
    private RecommendService recommendService;

    @Mock
    private LetterKeywordRepository letterKeywordRepository;

    @BeforeEach
    void setUp() {
        // 기본 설정
    }

    @Test
    @DisplayName("추천된 편지 ID 목록을 반환한다")
    void getRecommendedLetters() {
        // given
        List<String> userKeywords = List.of("사랑", "우정");
        List<Long> letterIds = List.of(1L, 2L, 3L, 4L);
        int limit = 2;

        List<Long> expectedRecommendedLetters = List.of(1L, 3L);

        when(letterKeywordRepository.getMatchedLetters(anyList(), anyList(), eq(limit)))
                .thenReturn(expectedRecommendedLetters);

        // when
        List<Long> result = recommendService.getRecommendedLetters(userKeywords, letterIds, limit);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(1L, 3L);

        verify(letterKeywordRepository, times(1)).getMatchedLetters(eq(userKeywords), eq(letterIds), eq(limit));
    }

    @Test
    @DisplayName("추천된 편지 ID 목록이 없을 경우 빈 리스트를 반환한다")
    void getRecommendedLettersEmpty() {
        // given
        List<String> userKeywords = List.of("행복", "추억");
        List<Long> letterIds = List.of(5L, 6L);
        int limit = 3;

        when(letterKeywordRepository.getMatchedLetters(anyList(), anyList(), eq(limit)))
                .thenReturn(List.of());

        // when
        List<Long> result = recommendService.getRecommendedLetters(userKeywords, letterIds, limit);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(letterKeywordRepository, times(1)).getMatchedLetters(eq(userKeywords), eq(letterIds), eq(limit));
    }
}
