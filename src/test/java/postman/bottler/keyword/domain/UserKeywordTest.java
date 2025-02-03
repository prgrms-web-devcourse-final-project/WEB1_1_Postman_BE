package postman.bottler.keyword.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserKeywordTest {

    @Test
    @DisplayName("UserKeyword 생성 - 모든 필드가 올바르게 설정되어야 한다.")
    void builder() {
        // given
        Long id = 1L;
        Long userId = 101L;
        String keyword = "Motivation";

        // when
        UserKeyword userKeyword = UserKeyword.builder()
                .id(id)
                .userId(userId)
                .keyword(keyword)
                .build();

        // then
        assertThat(userKeyword).isNotNull();
        assertThat(userKeyword.getId()).isEqualTo(id);
        assertThat(userKeyword.getUserId()).isEqualTo(userId);
        assertThat(userKeyword.getKeyword()).isEqualTo(keyword);
    }
}
