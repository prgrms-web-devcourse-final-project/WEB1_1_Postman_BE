package postman.bottler.notification.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import postman.bottler.notification.exception.NoLetterIdException;

@DisplayName("편지 알림 테스트")
public class LetterNotificationTest {
    @Test
    @DisplayName("편지 알림 생성 시, 편지 ID가 존재하지 않는 경우, 예외를 발생시킨다.")
    public void createLetterNotificationWithNonLetter() {
        Assertions.assertThatThrownBy(() -> Notification.create(NotificationType.NEW_LETTER, 1L, null))
                .isInstanceOf(NoLetterIdException.class);
    }
}
