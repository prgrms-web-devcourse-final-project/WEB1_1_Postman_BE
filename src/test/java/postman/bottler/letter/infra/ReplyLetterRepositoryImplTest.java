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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import postman.bottler.TestBase;
import postman.bottler.letter.domain.ReplyLetter;
import postman.bottler.letter.infra.entity.ReplyLetterEntity;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ReplyLetterRepositoryImplTest extends TestBase {

    @InjectMocks
    private ReplyLetterRepositoryImpl replyLetterRepository;

    @Mock
    private ReplyLetterJpaRepository replyLetterJpaRepository;

    private ReplyLetter replyLetter;
    private ReplyLetterEntity replyLetterEntity;

    @BeforeEach
    void setUp() {
        replyLetter = ReplyLetter.builder()
                .id(1L)
                .title("답장 제목")
                .content("답장 내용")
                .font("Arial")
                .paper("Simple")
                .label("중요")
                .letterId(100L)
                .receiverId(2L)
                .senderId(1L)
                .isDeleted(false)
                .isBlocked(false)
                .createdAt(LocalDateTime.now())
                .build();

        replyLetterEntity = ReplyLetterEntity.from(replyLetter);
    }

    @Test
    @DisplayName("답장 저장 테스트")
    void saveReplyLetter() {
        // given
        when(replyLetterJpaRepository.save(any(ReplyLetterEntity.class))).thenReturn(replyLetterEntity);

        // when
        ReplyLetter savedReplyLetter = replyLetterRepository.save(replyLetter);

        // then
        assertThat(savedReplyLetter).isNotNull();
        assertThat(savedReplyLetter.getTitle()).isEqualTo("답장 제목");
        verify(replyLetterJpaRepository, times(1)).save(any(ReplyLetterEntity.class));
    }

    @Test
    @DisplayName("특정 편지 ID와 수신자 ID로 답장 조회")
    void findAllByLetterIdAndReceiverId() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReplyLetterEntity> page = new PageImpl<>(List.of(replyLetterEntity));
        when(replyLetterJpaRepository.findAllByLetterIdAndReceiverId(100L, 2L, pageable)).thenReturn(page);

        // when
        Page<ReplyLetter> result = replyLetterRepository.findAllByLetterIdAndReceiverId(100L, 2L, pageable);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("답장 제목");
        verify(replyLetterJpaRepository, times(1)).findAllByLetterIdAndReceiverId(100L, 2L, pageable);
    }

    @Test
    @DisplayName("ID로 답장 조회 테스트")
    void findReplyLetterById() {
        // given
        when(replyLetterJpaRepository.findById(1L)).thenReturn(Optional.of(replyLetterEntity));

        // when
        Optional<ReplyLetter> result = replyLetterRepository.findById(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("답장 제목");
        verify(replyLetterJpaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("ID 목록으로 답장 삭제 테스트")
    void deleteReplyLettersByIds() {
        // when
        replyLetterRepository.deleteByIds(List.of(1L, 2L));

        // then
        verify(replyLetterJpaRepository, times(1)).deleteByIds(List.of(1L, 2L));
    }

    @Test
    @DisplayName("ID로 답장 차단 테스트")
    void blockReplyLetterById() {
        // when
        replyLetterRepository.blockReplyLetterById(1L);

        // then
        verify(replyLetterJpaRepository, times(1)).blockById(1L);
    }

    @Test
    @DisplayName("편지 ID와 발신자 ID로 답장 존재 여부 확인")
    void existsByLetterIdAndSenderId() {
        // given
        when(replyLetterJpaRepository.existsByLetterIdAndSenderId(100L, 1L)).thenReturn(true);

        // when
        boolean exists = replyLetterRepository.existsByLetterIdAndSenderId(100L, 1L);

        // then
        assertThat(exists).isTrue();
        verify(replyLetterJpaRepository, times(1)).existsByLetterIdAndSenderId(100L, 1L);
    }

    @Test
    @DisplayName("ID와 발신자 ID로 답장 존재 여부 확인")
    void existsByIdAndSenderId() {
        // given
        when(replyLetterJpaRepository.existsByIdAndSenderId(1L, 1L)).thenReturn(true);

        // when
        boolean exists = replyLetterRepository.existsByIdAndSenderId(1L, 1L);

        // then
        assertThat(exists).isTrue();
        verify(replyLetterJpaRepository, times(1)).existsByIdAndSenderId(1L, 1L);
    }
}
