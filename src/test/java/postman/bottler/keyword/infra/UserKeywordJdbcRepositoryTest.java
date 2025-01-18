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
import postman.bottler.keyword.domain.UserKeyword;

@SpringBootTest
@ActiveProfiles("test")
class UserKeywordJdbcRepositoryTest extends TestBase {

    @Autowired
    private UserKeywordJdbcRepository jdbcRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_SQL = "INSERT INTO user_keyword (user_id, keyword) VALUES (?, ?)";
    private static final String COUNT_SQL = "SELECT COUNT(*) FROM user_keyword WHERE user_id = ?";

    @BeforeEach
    void setUp() {
        jdbcTemplate.update(INSERT_SQL, 1L, "keyword1");
        jdbcTemplate.update(INSERT_SQL, 1L, "keyword2");
        jdbcTemplate.update(INSERT_SQL, 2L, "keyword3");
    }

    @Test
    @DisplayName("유저 키워드 일괄 삽입 테스트")
    void batchInsertKeywords() {
        // given
        List<UserKeyword> newKeywords = List.of(
                UserKeyword.builder().userId(3L).keyword("keyword4").build(),
                UserKeyword.builder().userId(3L).keyword("keyword5").build()
        );

        // when
        jdbcRepository.batchInsertKeywords(newKeywords);

        // then
        assertRowCountByUserId(3L, 2L); // 3L 유저의 키워드가 2개 삽입되었는지 확인
    }

    @Test
    @DisplayName("유저 ID로 키워드 삭제 테스트")
    void deleteAllByUserId() {
        // given
        Long userId = 1L;

        // when
        jdbcRepository.deleteAllByUserId(userId);

        // then
        assertRowCountByUserId(userId, 0L); // userId 1L의 키워드가 모두 삭제되었는지 확인
    }

    private void assertRowCountByUserId(Long userId, Long expectedCount) {
        Long actualCount = jdbcTemplate.queryForObject(COUNT_SQL, Long.class, userId);
        assertThat(actualCount).isEqualTo(expectedCount);
    }
}
