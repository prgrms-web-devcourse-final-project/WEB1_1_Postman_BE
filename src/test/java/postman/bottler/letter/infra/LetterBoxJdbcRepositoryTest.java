package postman.bottler.letter.infra;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import postman.bottler.TestBase;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterBox;
import postman.bottler.letter.domain.LetterType;

@SpringBootTest
@ActiveProfiles("test")
class LetterBoxJdbcRepositoryTest extends TestBase {

    @Autowired
    private LetterBoxJdbcRepository jdbcRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("LetterBox 저장")
    void saveLetterBox() {
        // given
        LetterBox letterBox = LetterBox.builder()
                .userId(1L)
                .letterId(101L)
                .letterType(LetterType.LETTER)
                .boxType(BoxType.SEND)
                .createdAt(LocalDateTime.now())
                .build();

        // when
        jdbcRepository.save(letterBox);

        // then
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM letter_box WHERE user_id = ? AND letter_id = ?",
                Long.class,
                letterBox.getUserId(),
                letterBox.getLetterId()
        );
        assertThat(count).isEqualTo(1L);
    }

    @Nested
    @DisplayName("LetterBox 존재 여부 확인")
    class ExistsByUserIdAndLetterIdTests {

        @Test
        @DisplayName("존재할 경우 true 반환")
        void returnTrueWhenLetterBoxExists() {
            // given
            LetterBox letterBox = LetterBox.builder()
                    .userId(2L)
                    .letterId(202L)
                    .letterType(LetterType.LETTER)
                    .boxType(BoxType.RECEIVE)
                    .createdAt(LocalDateTime.now())
                    .build();
            jdbcRepository.save(letterBox);

            // when
            boolean exists = jdbcRepository.existsByUserIdAndLetterId(202L, 2L);

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("존재하지 않을 경우 false 반환")
        void returnFalseWhenLetterBoxDoesNotExist() {
            // when
            boolean exists = jdbcRepository.existsByUserIdAndLetterId(999L, 999L);

            // then
            assertThat(exists).isFalse();
        }
    }
}
