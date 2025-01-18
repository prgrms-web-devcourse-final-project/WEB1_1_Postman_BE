package postman.bottler.letter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LetterTest {

    @Test
    @DisplayName("Letter 생성 - 모든 필드가 올바르게 설정되어야 한다")
    void createLetterWithValidFields() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        Letter letter = Letter.builder()
                .id(1L)
                .title("테스트 제목")
                .content("테스트 내용")
                .font("Pretendard")
                .paper("Classic")
                .label("라벨")
                .userId(100L)
                .isDeleted(false)
                .isBlocked(false)
                .createdAt(now)
                .build();

        // then
        assertThat(letter.getId()).isEqualTo(1L);
        assertThat(letter.getTitle()).isEqualTo("테스트 제목");
        assertThat(letter.getContent()).isEqualTo("테스트 내용");
        assertThat(letter.getFont()).isEqualTo("Pretendard");
        assertThat(letter.getPaper()).isEqualTo("Classic");
        assertThat(letter.getLabel()).isEqualTo("라벨");
        assertThat(letter.getUserId()).isEqualTo(100L);
        assertThat(letter.isDeleted()).isFalse();
        assertThat(letter.isBlocked()).isFalse();
        assertThat(letter.getCreatedAt()).isEqualTo(now);
    }
}
