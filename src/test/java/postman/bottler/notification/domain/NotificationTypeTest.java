package postman.bottler.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import postman.bottler.notification.exception.NoTypeException;

@DisplayName("알림 타입 테스트")
public class NotificationTypeTest {
    @Test
    @DisplayName("편지 관련 알림인지 확인한다.")
    public void isLetterNotification() {
        // GIVEN
        NotificationType newLetter = NotificationType.NEW_LETTER;
        NotificationType targetLetter = NotificationType.TARGET_LETTER;
        NotificationType mapReply = NotificationType.MAP_REPLY;
        NotificationType keywordReply = NotificationType.KEYWORD_REPLY;
        NotificationType warning = NotificationType.WARNING;
        NotificationType ban = NotificationType.BAN;

        // WHEN
        Boolean letter1 = newLetter.isLetterNotification();
        Boolean letter2 = targetLetter.isLetterNotification();
        Boolean letter3 = mapReply.isLetterNotification();
        Boolean letter4 = keywordReply.isLetterNotification();
        Boolean nonLetter1 = warning.isLetterNotification();
        Boolean nonLetter2 = ban.isLetterNotification();

        // THEN
        assertThat(letter1).isTrue();
        assertThat(letter2).isTrue();
        assertThat(letter3).isTrue();
        assertThat(nonLetter1).isFalse();
        assertThat(nonLetter2).isFalse();
    }

    @Test
    @DisplayName("해당하는 타입의 NotificationType을 반환한다.")
    public void from() {
        // GIVEN
        String newLetter = "NEW_LETTER";
        String targetLetter = "TARGET_LETTER";
        String mapReply = "MAP_REPLY";
        String keywordReply = "KEYWORD_REPLY";
        String warning = "WARNING";
        String ban = "BAN";

        // WHEN
        NotificationType newLetterType = NotificationType.from(newLetter);
        NotificationType targetLetterType = NotificationType.from(targetLetter);
        NotificationType mapReplyType = NotificationType.from(mapReply);
        NotificationType keywordReplyType = NotificationType.from(keywordReply);
        NotificationType warningType = NotificationType.from(warning);
        NotificationType banType = NotificationType.from(ban);

        // THEN
        assertThat(newLetterType).isEqualTo(NotificationType.NEW_LETTER);
        assertThat(targetLetterType).isEqualTo(NotificationType.TARGET_LETTER);
        assertThat(mapReplyType).isEqualTo(NotificationType.MAP_REPLY);
        assertThat(keywordReplyType).isEqualTo(NotificationType.KEYWORD_REPLY);
        assertThat(warningType).isEqualTo(NotificationType.WARNING);
        assertThat(banType).isEqualTo(NotificationType.BAN);
    }

    @Test
    @DisplayName("존재하지 않는 타입 변환 시도 시, NoTypeException 예외를 발생시킨다.")
    public void noType() {
        // GIVEN
        String nonNotification = "WRONG";

        // WHEN - THEN
        assertThatThrownBy(() -> NotificationType.from(nonNotification)).isInstanceOf(NoTypeException.class);
    }
}
