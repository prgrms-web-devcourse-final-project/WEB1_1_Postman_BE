package postman.bottler.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static postman.bottler.notification.domain.NotificationType.BAN;
import static postman.bottler.notification.domain.NotificationType.KEYWORD_REPLY;
import static postman.bottler.notification.domain.NotificationType.MAP_REPLY;
import static postman.bottler.notification.domain.NotificationType.NEW_LETTER;
import static postman.bottler.notification.domain.NotificationType.TARGET_LETTER;
import static postman.bottler.notification.domain.NotificationType.WARNING;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import postman.bottler.notification.exception.NoLetterIdException;

@DisplayName("알림 테스트")
public class NotificationTest {

    @Nested
    @DisplayName("알림 생성")
    class CreateNotification {
        @Test
        @DisplayName("새 편지 알림을 생성한다.")
        public void createNewLetterNotification() {
            // GIVEN
            NotificationType type = NEW_LETTER;
            Long receiver = 1L;
            Long letterId = 1L;
            String label = "label";

            // WHEN
            Notification notification = Notification.create(type, receiver, letterId, label);

            // THEN
            assertThat(notification).isInstanceOf(LetterNotification.class)
                    .extracting("type", "receiver", "isRead", "letterId")
                    .containsExactlyInAnyOrder(NEW_LETTER, 1L, false, 1L);
        }

        @Test
        @DisplayName("새 편지 알림 생성 시, 편지 ID가 없으면 예외가 발생한다.")
        public void createNewLetterWithoutLetterId() {
            // GIVEN
            NotificationType type = NEW_LETTER;
            Long receiver = 1L;
            Long letterId = null;
            String label = "label";

            // WHEN THEN
            assertThatThrownBy(
                    () -> Notification.create(type, receiver, letterId, label))
                    .isInstanceOf(NoLetterIdException.class);
        }

        @Test
        @DisplayName("타겟 편지 알림을 생성한다.")
        public void createTargetLetterNotification() {
            // GIVEN
            NotificationType type = TARGET_LETTER;
            Long receiver = 1L;
            Long letterId = 1L;
            String label = "label";

            // WHEN
            Notification notification = Notification.create(type, receiver, letterId, label);

            // THEN
            assertThat(notification).isInstanceOf(LetterNotification.class)
                    .extracting("type", "receiver", "isRead", "letterId")
                    .containsExactlyInAnyOrder(TARGET_LETTER, 1L, false, 1L);
        }

        @Test
        @DisplayName("타겟 편지 알림 생성 시, 편지 ID가 없으면 예외를 발생시킨다.")
        public void createTargetLetterNotificationWithoutLetterId() {
            // WHEN
            NotificationType type = TARGET_LETTER;
            Long receiver = 1L;
            Long letterId = null;
            String label = "label";

            // WHEN THEN
            assertThatThrownBy(
                    () -> Notification.create(type, receiver, letterId, label))
                    .isInstanceOf(NoLetterIdException.class);
        }

        @Test
        @DisplayName("지도 편지 답장 알림을 생성한다.")
        public void createReplyMapLetterNotification() {
            // GIVEN
            NotificationType type = MAP_REPLY;
            Long receiver = 1L;
            Long letterId = 1L;
            String label = "label";

            // WHEN
            Notification notification = Notification.create(type, receiver, letterId, label);

            // THEN
            assertThat(notification).isInstanceOf(LetterNotification.class)
                    .extracting("type", "receiver", "isRead", "letterId")
                    .containsExactlyInAnyOrder(MAP_REPLY, 1L, false, 1L);
        }

        @Test
        @DisplayName("지도 편지 답장 알림 생성 시, 편지 ID가 없으면 예외를 발생시킨다.")
        public void createReplyMapLetterNotificationWithoutLetterId() {
            // WHEN
            NotificationType type = TARGET_LETTER;
            Long receiver = 1L;
            Long letterId = null;
            String label = "label";

            // WHEN THEN
            assertThatThrownBy(
                    () -> Notification.create(type, receiver, letterId, label))
                    .isInstanceOf(NoLetterIdException.class);
        }

        @Test
        @DisplayName("키워드 편지 답장 알림을 생성한다.")
        public void createReplyKeywordLetterNotification() {
            // GIVEN
            NotificationType type = KEYWORD_REPLY;
            Long receiver = 1L;
            Long letterId = 1L;
            String label = "label";

            // WHEN
            Notification notification = Notification.create(type, receiver, letterId, label);

            // THEN
            assertThat(notification).isInstanceOf(LetterNotification.class)
                    .extracting("type", "receiver", "isRead", "letterId")
                    .containsExactlyInAnyOrder(KEYWORD_REPLY, 1L, false, 1L);
        }

        @Test
        @DisplayName("키워드 편지 답장 알림 생성 시, 편지 ID가 없으면 예외를 발생시킨다.")
        public void createReplyKeywordLetterNotificationWithoutLetterId() {
            // WHEN
            NotificationType type = KEYWORD_REPLY;
            Long receiver = 1L;
            Long letterId = null;
            String label = "label";

            // WHEN THEN
            assertThatThrownBy(
                    () -> Notification.create(type, receiver, letterId, label))
                    .isInstanceOf(NoLetterIdException.class);
        }

        @Test
        @DisplayName("유저 경고 알림을 생성한다.")
        public void warningNotificationTest() {
            // GIVEN
            NotificationType type = WARNING;
            Long receiver = 1L;
            Long letterId = null;
            String label = null;

            // WHEN
            Notification notification = Notification.create(type, receiver, letterId, label);

            // THEN
            assertThat(notification).isInstanceOf(Notification.class)
                    .isNotInstanceOf(LetterNotification.class)
                    .extracting("type", "receiver", "isRead")
                    .containsExactlyInAnyOrder(WARNING, 1L, false);
        }

        @Test
        @DisplayName("유저 정지 알림을 생성한다.")
        public void banNotificationTest() {
            // GIVEN
            NotificationType type = BAN;
            Long receiver = 1L;
            Long letterId = null;
            String label = null;

            // WHEN
            Notification notification = Notification.create(type, receiver, letterId, label);

            // THEN
            assertThat(notification).isInstanceOf(Notification.class)
                    .isNotInstanceOf(LetterNotification.class)
                    .extracting("type", "receiver", "isRead")
                    .containsExactlyInAnyOrder(BAN, 1L, false);
        }
    }

    @Test
    @DisplayName("알림을 읽는다면, 읽음 처리된 알림을 반환한다.")
    public void readNotification() {
        // GIVEN
        Notification notification = Notification.create(NEW_LETTER, 1L, 1L, "label");

        // WHEN
        Notification read = notification.read();

        // THEN
        assertThat(read).isNotEqualTo(notification)
                .extracting("type", "receiver", "createdAt", "letterId")
                .containsExactlyInAnyOrder(notification.getType(), notification.getReceiver(),
                        notification.getCreatedAt(), ((LetterNotification) notification).getLetterId());
        assertThat(read.getIsRead()).isTrue();
    }
}
