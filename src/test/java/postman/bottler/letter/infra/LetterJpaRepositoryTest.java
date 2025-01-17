package postman.bottler.letter.infra;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import postman.bottler.TestBase;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.infra.entity.LetterEntity;

@DataJpaTest
@ActiveProfiles("test")
class LetterJpaRepositoryTest extends TestBase {

    @Autowired
    private LetterJpaRepository letterJpaRepository;

    private LetterEntity letterEntity1;
    private LetterEntity letterEntity2;

    @BeforeEach
    void setUp() {
        // given
        Letter letter1 = Letter.builder()
                .title("Test Title 1")
                .content("Sample Content 1")
                .font("Arial")
                .paper("Simple")
                .label("Important")
                .userId(1L)
                .isDeleted(false)
                .isBlocked(false)
                .createdAt(LocalDateTime.now())
                .build();

        Letter letter2 = Letter.builder()
                .title("Test Title 2")
                .content("Sample Content 2")
                .font("Times New Roman")
                .paper("Fancy")
                .label("Work")
                .userId(2L)
                .isDeleted(false)
                .isBlocked(false)
                .createdAt(LocalDateTime.now())
                .build();

        letterEntity1 = LetterEntity.from(letter1);
        letterEntity2 = LetterEntity.from(letter2);

        letterJpaRepository.save(letterEntity1);
        letterJpaRepository.save(letterEntity2);
    }

    @Nested
    @DisplayName("LetterEntity 조회 테스트")
    class FindTests {

        @Test
        @DisplayName("삭제되지 않은 LetterEntity를 ID로 조회")
        void findById() {
            // when
            Optional<LetterEntity> result = letterJpaRepository.findById(letterEntity1.toDomain().getId());

            // then
            assertThat(result).isPresent();
            assertThat(result.get().toDomain().getTitle()).isEqualTo("Test Title 1");
        }

        @Test
        @DisplayName("특정 사용자 ID로 삭제되지 않은 모든 LetterEntity 조회")
        void findAllByUserId() {
            // when
            List<LetterEntity> results = letterJpaRepository.findAllByUserId(1L);

            // then
            assertThat(results).hasSize(1);
            assertThat(results.get(0).toDomain().getTitle()).isEqualTo("Test Title 1");
        }

        @Test
        @DisplayName("특정 LetterEntity ID 목록으로 삭제되지 않은 모든 LetterEntity 조회")
        void findAllByIds() {
            // when
            List<LetterEntity> results = letterJpaRepository.findAllByIds(List.of(
                    letterEntity1.toDomain().getId(),
                    letterEntity2.toDomain().getId()
            ));

            // then
            assertThat(results).hasSize(2);
            assertThat(results.get(0).toDomain().getTitle()).isEqualTo("Test Title 1");
            assertThat(results.get(1).toDomain().getTitle()).isEqualTo("Test Title 2");
        }
    }

    @Nested
    @DisplayName("LetterEntity 삭제 테스트")
    class DeleteTests {

        @Test
        @DisplayName("특정 LetterEntity ID 목록으로 soft delete 수행")
        void softDeleteByIds() {
            // when
            letterJpaRepository.softDeleteByIds(List.of(letterEntity1.toDomain().getId()));

            // then
            List<LetterEntity> results = letterJpaRepository.findAllByUserId(1L);
            assertThat(results).isEmpty();
        }

        @Test
        @DisplayName("특정 LetterEntity ID로 soft block 수행")
        void softBlockById() {
            // when
            letterJpaRepository.softBlockById(letterEntity1.toDomain().getId());

            // then
            Optional<LetterEntity> result = letterJpaRepository.findById(letterEntity1.toDomain().getId());
            assertThat(result).isEmpty();
        }
    }
}
