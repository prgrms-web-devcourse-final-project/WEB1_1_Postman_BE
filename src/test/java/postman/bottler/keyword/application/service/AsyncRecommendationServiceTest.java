package postman.bottler.keyword.application.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import postman.bottler.TestBase;
import postman.bottler.letter.application.service.LetterBoxService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AsyncRecommendationServiceTest extends TestBase {

    @InjectMocks
    private AsyncRecommendationService asyncRecommendationService;

    @Mock
    private RecommendService recommendService;

    @Mock
    private UserKeywordService userKeywordService;

    @Mock
    private LetterBoxService letterBoxService;

    @Mock
    private RedisLetterService redisLetterService;

    @Test
    @DisplayName("비동기 추천 처리가 성공적으로 수행된다")
    void processRecommendationForUser() {
        // given
        Long userId = 1L;

        List<String> mockKeywords = List.of("사랑", "행복", "우정");
        List<Long> mockReceivedLetters = List.of(101L, 102L, 103L);
        List<Long> mockRecommendedLetters = List.of(201L, 202L);

        when(userKeywordService.getKeywordsByUserId(userId)).thenReturn(mockKeywords);
        when(letterBoxService.findReceivedLettersByUserId(userId)).thenReturn(mockReceivedLetters);
        when(recommendService.getRecommendedLetters(mockKeywords, mockReceivedLetters, 10))
                .thenReturn(mockRecommendedLetters);

        // when
        asyncRecommendationService.processRecommendationForUser(userId);

        // then
        verify(userKeywordService, times(1)).getKeywordsByUserId(userId);
        verify(letterBoxService, times(1)).findReceivedLettersByUserId(userId);
        verify(recommendService, times(1)).getRecommendedLetters(mockKeywords, mockReceivedLetters, 10);
        verify(redisLetterService, times(1)).saveRecommendationsTemp(userId, mockRecommendedLetters);
    }
}
