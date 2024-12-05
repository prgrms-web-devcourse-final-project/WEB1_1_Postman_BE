package postman.bottler.keyword.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import postman.bottler.keyword.domain.UserKeyword;

@Repository
@RequiredArgsConstructor
public class UserKeywordJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsertKeywords(List<UserKeyword> keywords) {
        String sql = "INSERT INTO user_keyword (user_id, keyword) VALUES (?, ?)";

        List<Object[]> params = keywords.stream()
                .map(entity -> new Object[]{entity.getUserId(), entity.getKeyword()})
                .toList();

        jdbcTemplate.batchUpdate(sql, params);
    }

    public void deleteAllByUserId(Long userId) {
        String sql = "DELETE FROM user_keyword WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}
