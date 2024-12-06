package postman.bottler.letter.infra;

import java.sql.Timestamp;
import java.util.List;
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

    public void saveAll(List<LetterBox> letterBoxes) {
        String sql = "INSERT INTO letter_box (user_id, letter_id, letter_type, box_type, created_at) " +
                "VALUES (?, ?, ?, ?, ?)";

        List<Object[]> params = letterBoxes.stream()
                .map(box -> new Object[]{
                        box.getUserId(),
                        box.getLetterId(),
                        box.getLetterType().name(),
                        box.getBoxType().name(),
                        box.getCreatedAt() != null ? Timestamp.valueOf(box.getCreatedAt()) : null
                })
                .toList();

        jdbcTemplate.batchUpdate(sql, params);
    }
}
