package postman.bottler.keyword.infra;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import postman.bottler.TestBase;
import postman.bottler.keyword.domain.LetterKeyword;

@SpringBootTest
@ActiveProfiles("test")
class LetterKeywordJdbcRepositoryTest extends TestBase {

    @Autowired
    private LetterKeywordJdbcRepository jdbcRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String TRUNCATE_SQL = "TRUNCATE TABLE letter_keyword";
    private static final String COUNT_KEYWORDS_SQL = "SELECT COUNT(*) FROM letter_keyword WHERE letter_id = ?";
    private static final String COUNT_DELETED_KEYWORDS_SQL = "SELECT COUNT(*) FROM letter_keyword WHERE letter_id = ? AND is_deleted = true";

    private List<LetterKeyword> testKeywords;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute(TRUNCATE_SQL);

        testKeywords = List.of(
                LetterKeyword.from(1L, "keyword1"),
                LetterKeyword.from(1L, "keyword2"),
                LetterKeyword.from(2L, "keyword3")
        );
    }

    @Test
    @DisplayName("키워드를 일괄 삽입할 수 있다")
    void batchInsertKeywords() {
        // when
        jdbcRepository.batchInsertKeywords(testKeywords);

        // then
        assertCountByLetterId(1L, 2L);
    }

    @Test
    @DisplayName("키워드의 삭제 상태를 일괄 업데이트할 수 있다")
    void batchUpdateIsDeleted() {
        // given
        jdbcRepository.batchInsertKeywords(testKeywords);

        // when
        jdbcRepository.batchUpdateIsDeleted(List.of(1L));

        // then
        assertDeletedCountByLetterId(1L, 2L);
    }

    private void assertCountByLetterId(Long letterId, Long expectedCount) {
        Long actualCount = jdbcTemplate.queryForObject(COUNT_KEYWORDS_SQL, Long.class, letterId);
        assertThat(actualCount).isEqualTo(expectedCount);
    }

    private void assertDeletedCountByLetterId(Long letterId, Long expectedCount) {
        Long actualCount = jdbcTemplate.queryForObject(COUNT_DELETED_KEYWORDS_SQL, Long.class, letterId);
        assertThat(actualCount).isEqualTo(expectedCount);
    }
}
