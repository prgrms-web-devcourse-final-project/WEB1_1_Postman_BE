package postman.bottler.letter.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.infra.entity.LetterEntity;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class LetterRepositoryImplTest {

    @InjectMocks
    private LetterRepositoryImpl repository;

    @Mock
    private LetterJpaRepository letterJpaRepository;

    private Letter letter;
    private LetterEntity letterEntity;

    @BeforeEach
    void setUp() {
        letter = Letter.builder()
                .id(1L)
                .title("Test Letter")
                .content("This is a test letter.")
                .font("Arial")
                .paper("Simple")
                .label("Important")
                .userId(1L)
                .isDeleted(false)
                .isBlocked(false)
                .createdAt(LocalDateTime.now())
                .build();

        letterEntity = LetterEntity.from(letter);
    }

    @Test
    @DisplayName("Letter 저장")
    void saveLetter() {
        // given
        when(letterJpaRepository.save(any(LetterEntity.class))).thenReturn(letterEntity);

        // when
        Letter result = repository.save(letter);

        // then
        assertThat(result.getTitle()).isEqualTo(letter.getTitle());
        assertThat(result.getContent()).isEqualTo(letter.getContent());
        assertThat(result.getFont()).isEqualTo(letter.getFont());
        assertThat(result.getPaper()).isEqualTo(letter.getPaper());
        assertThat(result.getLabel()).isEqualTo(letter.getLabel());
        assertThat(result.getUserId()).isEqualTo(letter.getUserId());
        assertThat(result.isDeleted()).isEqualTo(letter.isDeleted());
        assertThat(result.isBlocked()).isEqualTo(letter.isBlocked());
        assertThat(result.getCreatedAt()).isEqualTo(letter.getCreatedAt());
        verify(letterJpaRepository, times(1)).save(any(LetterEntity.class));
    }

    @Nested
    @DisplayName("Letter 조회 테스트")
    class FindTests {

        @Test
        @DisplayName("ID로 Letter 조회")
        void findById() {
            // given
            when(letterJpaRepository.findById(1L)).thenReturn(Optional.of(letterEntity));

            // when
            Optional<Letter> result = repository.findById(1L);

            // then
            assertThat(result).isPresent();
            Letter retrieved = result.get();
            assertThat(retrieved.getTitle()).isEqualTo(letter.getTitle());
            assertThat(retrieved.getContent()).isEqualTo(letter.getContent());
            assertThat(retrieved.getFont()).isEqualTo(letter.getFont());
            assertThat(retrieved.getPaper()).isEqualTo(letter.getPaper());
            assertThat(retrieved.getLabel()).isEqualTo(letter.getLabel());
            assertThat(retrieved.getUserId()).isEqualTo(letter.getUserId());
            assertThat(retrieved.isDeleted()).isEqualTo(letter.isDeleted());
            assertThat(retrieved.isBlocked()).isEqualTo(letter.isBlocked());
            assertThat(retrieved.getCreatedAt()).isEqualTo(letter.getCreatedAt());
            verify(letterJpaRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("ID 리스트로 삭제되지 않은 Letter 조회")
        void findAllActiveByIds() {
            // given
            when(letterJpaRepository.findAllByIds(List.of(1L, 2L))).thenReturn(List.of(letterEntity));

            // when
            List<Letter> result = repository.findAllByIds(List.of(1L, 2L));

            // then
            assertThat(result).hasSize(1);
            Letter retrieved = result.get(0);
            assertThat(retrieved.getTitle()).isEqualTo(letter.getTitle());
            assertThat(retrieved.getContent()).isEqualTo(letter.getContent());
            assertThat(retrieved.getFont()).isEqualTo(letter.getFont());
            assertThat(retrieved.getPaper()).isEqualTo(letter.getPaper());
            assertThat(retrieved.getLabel()).isEqualTo(letter.getLabel());
            assertThat(retrieved.getUserId()).isEqualTo(letter.getUserId());
            assertThat(retrieved.isDeleted()).isEqualTo(letter.isDeleted());
            assertThat(retrieved.isBlocked()).isEqualTo(letter.isBlocked());
            assertThat(retrieved.getCreatedAt()).isEqualTo(letter.getCreatedAt());
            verify(letterJpaRepository, times(1)).findAllByIds(List.of(1L, 2L));
        }

        @Test
        @DisplayName("사용자 ID로 Letter 조회")
        void findAllByUserId() {
            // given
            when(letterJpaRepository.findAllByUserId(1L)).thenReturn(List.of(letterEntity));

            // when
            List<Letter> result = repository.findAllByUserId(1L);

            // then
            assertThat(result).hasSize(1);
            Letter retrieved = result.get(0);
            assertThat(retrieved.getTitle()).isEqualTo(letter.getTitle());
            assertThat(retrieved.getContent()).isEqualTo(letter.getContent());
            assertThat(retrieved.getFont()).isEqualTo(letter.getFont());
            assertThat(retrieved.getPaper()).isEqualTo(letter.getPaper());
            assertThat(retrieved.getLabel()).isEqualTo(letter.getLabel());
            assertThat(retrieved.getUserId()).isEqualTo(letter.getUserId());
            assertThat(retrieved.isDeleted()).isEqualTo(letter.isDeleted());
            assertThat(retrieved.isBlocked()).isEqualTo(letter.isBlocked());
            assertThat(retrieved.getCreatedAt()).isEqualTo(letter.getCreatedAt());
            verify(letterJpaRepository, times(1)).findAllByUserId(1L);
        }
    }

    @Nested
    @DisplayName("Letter 삭제 테스트")
    class DeleteTests {

        @Test
        @DisplayName("ID 리스트로 Letter 소프트 삭제")
        void softDeleteByIds() {
            // when
            repository.softDeleteByIds(List.of(1L, 2L));

            // then
            verify(letterJpaRepository, times(1)).softDeleteByIds(List.of(1L, 2L));
        }

        @Test
        @DisplayName("ID로 Letter 소프트 차단")
        void softBlockById() {
            // when
            repository.softBlockById(1L);

            // then
            verify(letterJpaRepository, times(1)).softBlockById(1L);
        }
    }

    @Test
    @DisplayName("ID로 Letter 존재 여부 확인")
    void existsById() {
        // given
        when(letterJpaRepository.existsById(1L)).thenReturn(true);

        // when
        boolean result = repository.existsById(1L);

        // then
        assertThat(result).isTrue();
        verify(letterJpaRepository, times(1)).existsById(1L);
    }
}
