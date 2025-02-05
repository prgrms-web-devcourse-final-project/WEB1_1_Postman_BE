package postman.bottler.letter.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LetterBoxTest {

    @Test
    @DisplayName("LetterBox 생성 - 모든 필드가 올바르게 설정되어야 한다")
    void createLetterBoxWithValidFields() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        LetterBox letterBox = LetterBox.builder()
                .id(1L)
                .userId(100L)
                .letterId(200L)
                .letterType(LetterType.LETTER)
                .boxType(BoxType.SEND)
                .createdAt(now)
                .build();

        // then
        assertThat(letterBox.getId()).isEqualTo(1L);
        assertThat(letterBox.getUserId()).isEqualTo(100L);
        assertThat(letterBox.getLetterId()).isEqualTo(200L);
        assertThat(letterBox.getLetterType()).isEqualTo(LetterType.LETTER);
        assertThat(letterBox.getBoxType()).isEqualTo(BoxType.SEND);
        assertThat(letterBox.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("LetterBox 생성 - UNKNOWN 타입으로 설정된 경우")
    void createLetterBoxWithUnknownType() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        LetterBox letterBox = LetterBox.builder()
                .id(2L)
                .userId(101L)
                .letterId(201L)
                .letterType(LetterType.NONE)
                .boxType(BoxType.UNKNOWN)
                .createdAt(now)
                .build();

        // then
        assertThat(letterBox.getId()).isEqualTo(2L);
        assertThat(letterBox.getUserId()).isEqualTo(101L);
        assertThat(letterBox.getLetterId()).isEqualTo(201L);
        assertThat(letterBox.getLetterType()).isEqualTo(LetterType.NONE);
        assertThat(letterBox.getBoxType()).isEqualTo(BoxType.UNKNOWN);
        assertThat(letterBox.getCreatedAt()).isEqualTo(now);
    }
}
