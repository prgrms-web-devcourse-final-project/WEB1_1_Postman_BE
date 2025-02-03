package postman.bottler.keyword.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import postman.bottler.keyword.domain.UserKeyword;

@Repository
@RequiredArgsConstructor
public class UserKeywordJdbcRepository {

    private static final String INSERT_SQL = "INSERT INTO user_keyword (user_id, keyword) VALUES (?, ?)";
    private static final String DELETE_SQL = "DELETE FROM user_keyword WHERE user_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public void batchInsertKeywords(List<UserKeyword> keywords) {
        List<Object[]> batchKeywordParams = toBatchKeywordParams(keywords);

        jdbcTemplate.batchUpdate(INSERT_SQL, batchKeywordParams);
    }

    public void deleteAllByUserId(Long userId) {
        jdbcTemplate.update(DELETE_SQL, userId);
    }

    private List<Object[]> toBatchKeywordParams(List<UserKeyword> keywords) {
        return keywords.stream()
                .map(entity -> new Object[]{entity.getUserId(), entity.getKeyword()})
                .toList();
    }
}
