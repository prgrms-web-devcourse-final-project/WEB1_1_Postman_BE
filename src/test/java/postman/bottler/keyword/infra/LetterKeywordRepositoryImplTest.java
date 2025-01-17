package postman.bottler.keyword.infra;

import static org.assertj.core.api.Assertions.assertThat;
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
import postman.bottler.keyword.domain.LetterKeyword;
import postman.bottler.keyword.infra.entity.LetterKeywordEntity;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class LetterKeywordRepositoryImplTest extends TestBase {

    @InjectMocks
    private LetterKeywordRepositoryImpl repository;

    @Mock
    private LetterKeywordQueryDslRepository queryDslRepository;

    @Mock
    private LetterKeywordJdbcRepository jdbcRepository;

    private static final List<LetterKeyword> MOCK_KEYWORDS = List.of(
            LetterKeyword.from(1L, "keyword1"),
            LetterKeyword.from(1L, "keyword2")
    );

    private static final List<Long> MOCK_MATCHED_LETTER_IDS = List.of(1L, 2L);

    private static final List<LetterKeywordEntity> MOCK_KEYWORD_ENTITIES = List.of(
            LetterKeywordEntity.builder().letterId(1L).keyword("keyword1").isDeleted(false).build(),
            LetterKeywordEntity.builder().letterId(1L).keyword("keyword2").isDeleted(false).build()
    );

    @Test
    @DisplayName("키워드 리스트 저장")
    void saveAll() {
        // given
        when(jdbcRepository.batchInsertKeywords(MOCK_KEYWORDS)).thenReturn(MOCK_KEYWORDS);

        // when
        List<LetterKeyword> savedKeywords = repository.saveAll(MOCK_KEYWORDS);

        // then
        assertThat(savedKeywords).isEqualTo(MOCK_KEYWORDS);
        verify(jdbcRepository, times(1)).batchInsertKeywords(MOCK_KEYWORDS);
    }

    @Test
    @DisplayName("특정 LetterId로 키워드 조회")
    void getKeywordsByLetterId() {
        // given
        Long letterId = 1L;
        when(queryDslRepository.findKeywordsByLetterId(letterId)).thenReturn(MOCK_KEYWORD_ENTITIES);

        // when
        List<LetterKeyword> keywords = repository.getKeywordsByLetterId(letterId);

        // then
        assertThat(keywords).hasSize(2);
        assertThat(keywords.get(0).getKeyword()).isEqualTo("keyword1");
        verify(queryDslRepository, times(1)).findKeywordsByLetterId(letterId);
    }

    @Test
    @DisplayName("유저 키워드와 일치하는 LetterId 조회")
    void getMatchedLetters() {
        // given
        List<String> userKeywords = List.of("keyword1", "keyword2");
        int limit = 5;
        when(queryDslRepository.getMatchedLetters(userKeywords, MOCK_MATCHED_LETTER_IDS, limit))
                .thenReturn(MOCK_MATCHED_LETTER_IDS);

        // when
        List<Long> matchedLetters = repository.getMatchedLetters(userKeywords, MOCK_MATCHED_LETTER_IDS, limit);

        // then
        assertThat(matchedLetters).isEqualTo(MOCK_MATCHED_LETTER_IDS);
        verify(queryDslRepository, times(1)).getMatchedLetters(userKeywords, MOCK_MATCHED_LETTER_IDS, limit);
    }

    @Test
    @DisplayName("키워드 삭제 상태로 변경")
    void markKeywordsAsDeleted() {
        // given
        List<Long> letterIds = List.of(1L, 2L);

        // when
        repository.markKeywordsAsDeleted(letterIds);

        // then
        verify(jdbcRepository, times(1)).batchUpdateIsDeleted(letterIds);
    }

    @Test
    @DisplayName("자주 사용된 키워드 조회")
    void getFrequentKeywords() {
        // given
        List<Long> letterIds = List.of(1L, 2L);
        when(queryDslRepository.getFrequentKeywords(letterIds)).thenReturn(MOCK_KEYWORD_ENTITIES);

        // when
        List<LetterKeyword> frequentKeywords = repository.getFrequentKeywords(letterIds);

        // then
        assertThat(frequentKeywords).hasSize(2);
        assertThat(frequentKeywords.get(0).getKeyword()).isEqualTo("keyword1");
        verify(queryDslRepository, times(1)).getFrequentKeywords(letterIds);
    }
}
