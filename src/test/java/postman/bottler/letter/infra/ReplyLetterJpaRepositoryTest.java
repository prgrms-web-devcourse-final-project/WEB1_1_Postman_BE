package postman.bottler.letter.infra;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import postman.bottler.TestBase;
import postman.bottler.letter.domain.ReplyLetter;
import postman.bottler.letter.infra.entity.ReplyLetterEntity;

@DataJpaTest
@ActiveProfiles("test")
class ReplyLetterJpaRepositoryTest extends TestBase {

    @Autowired
    private ReplyLetterJpaRepository repository;

    private ReplyLetterEntity replyLetterEntity;

    @BeforeEach
    void setUp() {
        replyLetterEntity = ReplyLetterEntity.builder()
                .title("테스트 답장")
                .content("이것은 테스트 답장입니다.")
                .font("Arial")
                .paper("Simple")
                .label("긴급")
                .letterId(101L)
                .receiverId(1L)
                .senderId(2L)
                .isDeleted(false)
                .isBlocked(false)
                .createdAt(LocalDateTime.now())
                .build();
        repository.save(replyLetterEntity);
    }

    @Test
    @DisplayName("편지 ID와 수신자 ID로 답장 목록 조회")
    void findAllByLetterIdAndReceiverId() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<ReplyLetterEntity> result = repository.findAllByLetterIdAndReceiverId(101L, 1L, pageable);

        // then
        assertThat(result).isNotEmpty();
        ReplyLetter domainResult = result.getContent().get(0).toDomain();
        assertThat(domainResult.getTitle()).isEqualTo("테스트 답장");
        assertThat(domainResult.getReceiverId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("ID로 답장 조회")
    void findById() {
        // when
        Optional<ReplyLetterEntity> result = repository.findById(replyLetterEntity.toDomain().getId());

        // then
        assertThat(result).isPresent();
        ReplyLetter domainResult = result.get().toDomain();
        assertThat(domainResult.getTitle()).isEqualTo("테스트 답장");
    }

    @Test
    @DisplayName("ID 목록으로 답장 삭제")
    void softDeleteByIds() {
        // when
        repository.softDeleteByIds(List.of(replyLetterEntity.toDomain().getId()));

        // then
        Optional<ReplyLetterEntity> result = repository.findById(replyLetterEntity.toDomain().getId());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("ID로 답장 차단")
    void softBlockById() {
        // when
        repository.softBlockById(replyLetterEntity.toDomain().getId());

        // then
        Optional<ReplyLetterEntity> result = repository.findById(replyLetterEntity.toDomain().getId());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("편지 ID와 발신자 ID로 답장 존재 여부 확인")
    void existsByLetterIdAndSenderId() {
        // when
        boolean exists = repository.existsByLetterIdAndSenderId(101L, 2L);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("ID와 발신자 ID로 답장 존재 여부 확인")
    void existsByIdAndSenderId() {
        // when
        boolean exists = repository.existsByIdAndSenderId(replyLetterEntity.toDomain().getId(), 2L);

        // then
        assertThat(exists).isTrue();
    }
}
