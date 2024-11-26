package postman.bottler.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("알림 타입 테스트")
public class NotificationTypeTest {
    @Test
    @DisplayName("편지 관련 알림인지 확인한다.")
    public void isLetterNotification() {
        // GIVEN
        NotificationType newLetter = NotificationType.NEW_LETTER;
        NotificationType targetLetter = NotificationType.TARGET_LETTER;
        NotificationType replyLetter = NotificationType.REPLY_LETTER;
        NotificationType warning = NotificationType.WARNING;
        NotificationType ban = NotificationType.BAN;

        // WHEN
        Boolean letter1 = newLetter.isLetterNotification();
        Boolean letter2 = targetLetter.isLetterNotification();
        Boolean letter3 = replyLetter.isLetterNotification();
        Boolean nonLetter1 = warning.isLetterNotification();
        Boolean nonLetter2 = ban.isLetterNotification();

        // THEN
        assertThat(letter1).isTrue();
        assertThat(letter2).isTrue();
        assertThat(letter3).isTrue();
        assertThat(nonLetter1).isFalse();
        assertThat(nonLetter2).isFalse();
    }
}
