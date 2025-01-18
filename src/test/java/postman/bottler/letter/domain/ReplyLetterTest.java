package postman.bottler.letter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReplyLetterTest {

    @Test
    @DisplayName("ReplyLetter 생성 - 모든 필드가 올바르게 설정되어야 한다")
    void createReplyLetterWithValidFields() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        ReplyLetter replyLetter = ReplyLetter.builder()
                .id(1L)
                .title("답장 제목")
                .content("답장 내용")
                .font("Pretendard")
                .paper("Classic")
                .label("라벨")
                .letterId(100L)
                .receiverId(200L)
                .senderId(300L)
                .isDeleted(false)
                .isBlocked(false)
                .createdAt(now)
                .build();

        // then
        assertThat(replyLetter.getId()).isEqualTo(1L);
        assertThat(replyLetter.getTitle()).isEqualTo("답장 제목");
        assertThat(replyLetter.getContent()).isEqualTo("답장 내용");
        assertThat(replyLetter.getFont()).isEqualTo("Pretendard");
        assertThat(replyLetter.getPaper()).isEqualTo("Classic");
        assertThat(replyLetter.getLabel()).isEqualTo("라벨");
        assertThat(replyLetter.getLetterId()).isEqualTo(100L);
        assertThat(replyLetter.getReceiverId()).isEqualTo(200L);
        assertThat(replyLetter.getSenderId()).isEqualTo(300L);
        assertThat(replyLetter.isDeleted()).isFalse();
        assertThat(replyLetter.isBlocked()).isFalse();
        assertThat(replyLetter.getCreatedAt()).isEqualTo(now);
    }
}
