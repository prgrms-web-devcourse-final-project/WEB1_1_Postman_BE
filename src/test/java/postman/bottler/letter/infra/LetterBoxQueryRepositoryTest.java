package postman.bottler.letter.infra;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import postman.bottler.TestBase;
import postman.bottler.config.QueryDslConfig;
import postman.bottler.letter.application.dto.response.LetterSummaryResponseDTO;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.domain.LetterBox;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.domain.ReplyLetter;
import postman.bottler.letter.infra.entity.LetterBoxEntity;
import postman.bottler.letter.infra.entity.LetterEntity;
import postman.bottler.letter.infra.entity.ReplyLetterEntity;

@DataJpaTest
@ActiveProfiles("test")
@Import({LetterBoxQueryRepository.class, QueryDslConfig.class})
class LetterBoxQueryRepositoryTest extends TestBase {

    @Autowired
    private LetterBoxQueryRepository repository;

    @Autowired
    private TestEntityManager em;

    private LetterEntity letterEntity;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        Letter letter = Letter.builder()
                .title("Test Letter")
                .content("Sample content")
                .font("Arial")
                .paper("Simple")
                .label("Important")
                .userId(1L)
                .isDeleted(false)
                .isBlocked(false)
                .createdAt(LocalDateTime.now())
                .build();

        letterEntity = LetterEntity.from(letter);
        em.persist(letterEntity);

        ReplyLetter replyLetter = ReplyLetter.builder()
                .title("Test Letter")
                .content("Sample content")
                .font("Arial")
                .paper("Simple")
                .label("Important")
                .receiverId(1L)
                .senderId(1L)
                .isDeleted(false)
                .isBlocked(false)
                .createdAt(LocalDateTime.now())
                .build();

        ReplyLetterEntity replyLetterEntity = ReplyLetterEntity.from(replyLetter);
        em.persist(replyLetterEntity);

        LetterBox letterReceive = LetterBox.builder()
                .userId(1L)
                .letterId(letterEntity.toDomain().getId())
                .letterType(LetterType.LETTER)
                .boxType(BoxType.RECEIVE)
                .createdAt(LocalDateTime.now())
                .build();
        em.persist(LetterBoxEntity.from(letterReceive));

        LetterBox letterSend = LetterBox.builder()
                .userId(1L)
                .letterId(letterEntity.toDomain().getId())
                .letterType(LetterType.LETTER)
                .boxType(BoxType.SEND)
                .createdAt(LocalDateTime.now())
                .build();
        em.persist(LetterBoxEntity.from(letterSend));

        LetterBox replyReceive = LetterBox.builder()
                .userId(1L)
                .letterId(replyLetterEntity.toDomain().getId())
                .letterType(LetterType.REPLY_LETTER)
                .boxType(BoxType.RECEIVE)
                .createdAt(LocalDateTime.now())
                .build();
        em.persist(LetterBoxEntity.from(replyReceive));

        LetterBox replySend = LetterBox.builder()
                .userId(1L)
                .letterId(replyLetterEntity.toDomain().getId())
                .letterType(LetterType.REPLY_LETTER)
                .boxType(BoxType.SEND)
                .createdAt(LocalDateTime.now())
                .build();
        em.persist(LetterBoxEntity.from(replySend));
    }

    @Nested
    @DisplayName("보유한 키워드 편지 전체 조회")
    class FetchLettersTests {

        @Test
        @DisplayName("사용자가 보유한 편지 조회")
        void fetchLetters() {
            // when
            List<LetterSummaryResponseDTO> letters = repository.fetchLetters(1L, BoxType.SEND, pageable);
            for (LetterSummaryResponseDTO letter : letters) {
                System.out.println("letter = " + letter.toString());
            }
            // then
            assertThat(letters).isNotEmpty();
            assertThat(letters.get(0).title()).isEqualTo("Test Letter");
            assertThat(letters.get(0).label()).isEqualTo("Important");
        }

        @Test
        @DisplayName("사용자가 없을 경우 빈 목록 반환")
        void returnEmptyWhenNoUserExists() {
            // when
            List<LetterSummaryResponseDTO> letters = repository.fetchLetters(999L, BoxType.RECEIVE, pageable);

            // then
            assertThat(letters).isEmpty();
        }
    }

    @Nested
    @DisplayName("보유한 받은 키워드 편지 조회")
    class FindReceivedLettersByIdTests {

        @Test
        @DisplayName("사용자가 보유한 모든 받은 편지 조회")
        void findReceivedLettersById() {
            // when
            List<Long> letterIds = repository.findReceivedLettersById(1L);

            // then
            assertThat(letterIds.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("받은 편지가 없을 경우 빈 목록 반환")
        void returnEmptyWhenNoReceivedLettersExist() {
            // when
            List<Long> letterIds = repository.findReceivedLettersById(999L);

            // then
            assertThat(letterIds).isEmpty();
        }
    }

    @Nested
    @DisplayName("편지 개수 조회")
    class CountLettersTests {

        @Test
        @DisplayName("특정 BoxType의 편지 수를 조회한다")
        void countLettersSuccessfully() {
            // when
            long count = repository.countLetters(1L, BoxType.RECEIVE);

            // then
            assertThat(count).isEqualTo(2);
        }

        @Test
        @DisplayName("특정 BoxType의 편지가 없으면 0 반환")
        void returnZeroWhenNoLettersExist() {
            // when
            long count = repository.countLetters(999L, BoxType.RECEIVE);

            // then
            assertThat(count).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("보관함에 있는 편지 삭제")
    class DeleteByConditionTests {

        @Nested
        @DisplayName("LETTER 타입 편지면서")
        class LetterTypeTests {

            @Test
            @DisplayName("SEND 타입인 편지 삭제")
            void deleteLetterTypeSend() {
                // when
                repository.deleteByCondition(List.of(letterEntity.toDomain().getId()), LetterType.LETTER, BoxType.SEND);

                // then
                List<LetterSummaryResponseDTO> letters = repository.fetchLetters(1L, BoxType.SEND, pageable);
                assertThat(letters.size()).isEqualTo(1);
            }

            @Test
            @DisplayName("RECEIVE 타입인 편지 삭제")
            void deleteLetterTypeReceive() {
                // when
                repository.deleteByCondition(List.of(letterEntity.toDomain().getId()), LetterType.LETTER,
                        BoxType.RECEIVE);

                // then
                List<LetterSummaryResponseDTO> letters = repository.fetchLetters(1L, BoxType.RECEIVE, pageable);
                assertThat(letters.size()).isEqualTo(1);
            }
        }

        @Nested
        @DisplayName("REPLY_LETTER 타입 편지면서")
        class ReplyLetterTypeTests {

            @Test
            @DisplayName("SEND 타입인 편지 삭제")
            void deleteReplyLetterTypeSend() {
                // when
                repository.deleteByCondition(List.of(letterEntity.toDomain().getId()), LetterType.REPLY_LETTER,
                        BoxType.SEND);

                // then
                List<LetterSummaryResponseDTO> letters = repository.fetchLetters(1L, BoxType.SEND, pageable);
                assertThat(letters.size()).isEqualTo(1);
            }

            @Test
            @DisplayName("RECEIVE 타입인 편지 삭제")
            void deleteReplyLetterTypeReceive() {
                // when
                repository.deleteByCondition(List.of(letterEntity.toDomain().getId()), LetterType.REPLY_LETTER,
                        BoxType.RECEIVE);

                // then
                List<LetterSummaryResponseDTO> letters = repository.fetchLetters(1L, BoxType.RECEIVE, pageable);
                assertThat(letters.size()).isEqualTo(1);
            }

            @Test
            @DisplayName("UNKNOWN 타입일 경우 타입 조건 무시 후 편지 삭제")
            void deleteReplyLetterTypeUnknown() {
                // when
                repository.deleteByCondition(List.of(letterEntity.toDomain().getId()), LetterType.UNKNOWN,
                        BoxType.RECEIVE);

                // then
                List<LetterSummaryResponseDTO> letters = repository.fetchLetters(1L, BoxType.RECEIVE, pageable);
                assertThat(letters).isEmpty();
            }
        }

        @Test
        @DisplayName("존재하지 않는 ID는 삭제되지 않는다")
        void notDeleteNonExistingLetterId() {
            // when
            repository.deleteByCondition(List.of(999L), LetterType.LETTER, BoxType.RECEIVE);

            // then
            List<LetterSummaryResponseDTO> letterIds = repository.fetchLetters(1L, BoxType.RECEIVE, pageable);
            assertThat(letterIds.size()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("특정 사용자의 타입별 편지 삭제")
    class DeleteByConditionAndUserIdTests {

        @Test
        @DisplayName("특정 사용자와 조건에 맞는 편지를 삭제한다")
        void deleteByConditionAndUserId() {
            // when
            repository.deleteByConditionAndUserId(List.of(letterEntity.toDomain().getId()), LetterType.LETTER,
                    BoxType.RECEIVE, 1L);

            // then
            List<LetterSummaryResponseDTO> letters = repository.fetchLetters(1L, BoxType.RECEIVE, pageable);
            assertThat(letters.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("조건에 맞는 편지가 없으면 아무 작업도 하지 않는다")
        void doNothingWhenNoLettersMatchConditionAndUserId() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            // when
            repository.deleteByConditionAndUserId(List.of(999L), LetterType.LETTER, BoxType.RECEIVE, 1L);

            // then
            List<LetterSummaryResponseDTO> letters = repository.fetchLetters(1L, BoxType.RECEIVE, pageable);
            assertThat(letters.size()).isEqualTo(2);
        }
    }
}
