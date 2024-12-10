package postman.bottler.keyword.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import postman.bottler.keyword.domain.LetterKeyword;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LetterKeywordJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsertKeywords(List<LetterKeyword> keywords) {
        String sql = "INSERT INTO letter_keyword (letter_id, keyword, is_deleted) VALUES (?, ?, ?)";

        List<Object[]> params = keywords.stream()
                .map(keyword -> new Object[]{keyword.getLetterId(), keyword.getKeyword(), keyword.isDeleted()})
                .toList();

        jdbcTemplate.batchUpdate(sql, params);
    }

    public void batchUpdateIsDeleted(List<Long> letterIds) {
        String sql = "UPDATE letter_keyword SET is_deleted = true WHERE letter_id = ?";

        List<Object[]> batchArgs = letterIds.stream()
                .map(id -> new Object[]{id})
                .toList();

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

}
