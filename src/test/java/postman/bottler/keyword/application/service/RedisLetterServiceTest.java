package postman.bottler.keyword.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import postman.bottler.TestBase;
import postman.bottler.keyword.util.RedisLetterKeyUtil;
import postman.bottler.letter.application.dto.LetterBoxDTO;
import postman.bottler.letter.application.service.LetterBoxService;
import postman.bottler.letter.application.service.LetterService;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.exception.LetterNotFoundException;
import postman.bottler.notification.application.dto.request.RecommendNotificationRequestDTO;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RedisLetterServiceTest extends TestBase {

    @InjectMocks
    private RedisLetterService redisLetterService;

    @Mock
    private RedisTemplate<String, List<Long>> redisTemplate;

    @Mock
    private ValueOperations<String, List<Long>> valueOperations;

    @Mock
    private LetterBoxService letterBoxService;

    @Mock
    private LetterService letterService;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("임시 추천 저장")
    void saveTempRecommendations() {
        // given
        Long userId = 1L;
        List<Long> recommendations = List.of(101L, 102L, 103L);
        String key = RedisLetterKeyUtil.getTempRecommendationKey(userId);

        // when
        redisLetterService.saveTempRecommendations(userId, recommendations);

        // then
        verify(valueOperations, times(1)).set(eq(key), eq(recommendations));
    }

    @Test
    @DisplayName("개발자 추천 저장")
    void saveDeveloperLetter() {
        // given
        Long userId = 1L;
        List<Long> recommendations = List.of(201L, 202L);
        String key = RedisLetterKeyUtil.getActiveRecommendationKey(userId);

        // when
        redisLetterService.saveDeveloperLetter(userId, recommendations);

        // then
        verify(valueOperations, times(1)).set(eq(key), eq(recommendations));
    }

    @Test
    @DisplayName("임시 추천 업데이트 및 알림 생성")
    void updateRecommendationsFromTemp() {
        // given
        Long userId = 1L;
        String tempKey = RedisLetterKeyUtil.getTempRecommendationKey(userId);
        String activeKey = RedisLetterKeyUtil.getActiveRecommendationKey(userId);

        List<Long> tempRecommendations = List.of(301L, 302L);
        List<Long> activeRecommendations = new ArrayList<>(List.of(303L));

        when(valueOperations.get(tempKey)).thenReturn(tempRecommendations);
        when(valueOperations.get(activeKey)).thenReturn(activeRecommendations);
        when(letterService.letterExists(301L)).thenReturn(true);

        Letter mockLetter = Letter.builder().id(301L).label("testLabel").build();
        when(letterService.findLetter(301L)).thenReturn(mockLetter);

        // when
        RecommendNotificationRequestDTO result = redisLetterService.updateRecommendationsFromTemp(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.letterId()).isEqualTo(301L);
        assertThat(result.label()).isEqualTo("testLabel");
        verify(letterBoxService, times(1)).saveLetter(any(LetterBoxDTO.class));
        verify(valueOperations, times(1)).set(eq(activeKey), any());
        verify(redisTemplate, times(1)).delete(eq(tempKey));
    }

    @Test
    @DisplayName("임시 추천에서 유효한 편지가 없을 경우 예외 발생")
    void updateRecommendationsFromTempNoValidLetters() {
        // given
        Long userId = 1L;
        String tempKey = RedisLetterKeyUtil.getTempRecommendationKey(userId);

        List<Long> tempRecommendations = List.of(401L, 402L);
        when(valueOperations.get(tempKey)).thenReturn(tempRecommendations);
        when(letterService.letterExists(anyLong())).thenReturn(false);

        // when & then
        assertThrows(LetterNotFoundException.class, () -> redisLetterService.updateRecommendationsFromTemp(userId));
    }

    @Test
    @DisplayName("추천 목록 조회")
    void fetchActiveRecommendations() {
        // given
        Long userId = 1L;
        String activeKey = RedisLetterKeyUtil.getActiveRecommendationKey(userId);
        List<Long> recommendations = List.of(501L, 502L);

        when(valueOperations.get(activeKey)).thenReturn(recommendations);

        // when
        List<Long> result = redisLetterService.fetchActiveRecommendations(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).containsExactly(501L, 502L);
        verify(valueOperations, times(1)).get(eq(activeKey));
    }

    @Test
    @DisplayName("임시 추천 목록 조회")
    void getRecommendedTemp() {
        // given
        Long userId = 1L;
        String tempKey = RedisLetterKeyUtil.getTempRecommendationKey(userId);
        List<Long> tempRecommendations = List.of(601L, 602L);

        when(valueOperations.get(tempKey)).thenReturn(tempRecommendations);

        // when
        List<Long> result = redisLetterService.fetchTempRecommendations(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).containsExactly(601L, 602L);
        verify(valueOperations, times(1)).get(eq(tempKey));
    }
}
