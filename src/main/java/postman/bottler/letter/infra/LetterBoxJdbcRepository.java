package postman.bottler.letter.infra;

import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import postman.bottler.letter.domain.LetterBox;

@Repository
@RequiredArgsConstructor
public class LetterBoxJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void save(LetterBox letterBox) {
        String sql = "INSERT INTO letter_box (user_id, letter_id, letter_type, box_type, created_at) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                letterBox.getUserId(),
                letterBox.getLetterId(),
                letterBox.getLetterType().name(),
                letterBox.getBoxType().name(),
                letterBox.getCreatedAt() != null ? Timestamp.valueOf(letterBox.getCreatedAt()) : null
        );
    }

    public boolean existsByUserIdAndLetterId(Long letterId, Long userId) {
        String sql = "SELECT COUNT(*) FROM letter_box WHERE user_id = ? AND letter_id = ?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, letterId);
        return count > 0;
    }
}
