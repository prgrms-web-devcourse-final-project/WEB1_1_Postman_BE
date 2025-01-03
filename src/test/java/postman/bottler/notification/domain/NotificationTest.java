package postman.bottler.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import postman.bottler.notification.application.dto.request.NotificationRequestDTO;
import postman.bottler.notification.exception.NoLetterIdException;
import postman.bottler.notification.exception.NoTypeException;

@DisplayName("알림 테스트")
public class NotificationTest {

    @Test
    @DisplayName("잘못된 편지 타입 요청 시, NoTypeException을 발생시킨다.")
    public void wrongType() {
        // GIVEN
        NotificationRequestDTO wrong = new NotificationRequestDTO("WRONG", 1L, 1L, null);

        // WHEN - THEN
        assertThatThrownBy(() -> Notification.create(NotificationType.from(wrong.notificationType()), wrong.receiver(),
                wrong.letterId(), wrong.label()))
                .isInstanceOf(NoTypeException.class);
    }

    @Nested
    @DisplayName("알림 생성")
    class CreateNotification {
        @Test
        @DisplayName("새 편지 알림을 생성한다.")
        public void newLetterNotificationTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("NEW_LETTER", 1L, 1L, "label");

            // WHEN
            Notification notification = Notification.create(NotificationType.from(request.notificationType()),
                    request.receiver(), request.letterId(), request.label());

            // THEN
            assertThat(notification.getType()).isEqualTo(NotificationType.NEW_LETTER);
            assertThat(notification.getReceiver()).isEqualTo(1L);
            assertThat(notification).isInstanceOf(LetterNotification.class);
            assertThat(((LetterNotification) notification).getLetterId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("새 편지 생성 시, 편지 ID가 없으면 예외를 발생시킨다.")
        public void newLetterNoLetterIdTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("NEW_LETTER", 1L, null, null);

            // WHEN
            assertThatThrownBy(
                    () -> Notification.create(NotificationType.from(request.notificationType()), request.receiver(),
                            request.letterId(), request.label()))
                    .isInstanceOf(NoLetterIdException.class);
        }

        @Test
        @DisplayName("타겟 편지 알림을 생성한다.")
        public void targetLetterNotificationTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("TARGET_LETTER", 1L, 1L, "label");

            // WHEN
            Notification notification = Notification.create(NotificationType.from(request.notificationType()),
                    request.receiver(), request.letterId(), request.label());

            // THEN
            assertThat(notification.getType()).isEqualTo(NotificationType.TARGET_LETTER);
            assertThat(notification.getReceiver()).isEqualTo(1L);
            assertThat(notification).isInstanceOf(LetterNotification.class);
            assertThat(((LetterNotification) notification).getLetterId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("타겟 편지 생성 시, 편지 ID가 없으면 예외를 발생시킨다.")
        public void targetLetterNoLetterIdTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("TARGET_LETTER", 1L, null, null);

            // WHEN
            assertThatThrownBy(
                    () -> Notification.create(NotificationType.from(request.notificationType()), request.receiver(),
                            request.letterId(), request.label()))
                    .isInstanceOf(NoLetterIdException.class);
        }

        @Test
        @DisplayName("지도 편지 답장 알림을 생성한다.")
        public void replyMapLetterNotificationTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("MAP_REPLY", 1L, 1L, null);

            // WHEN
            Notification notification = Notification.create(NotificationType.from(request.notificationType()),
                    request.receiver(), request.letterId(), null);

            // THEN
            assertThat(notification.getType()).isEqualTo(NotificationType.MAP_REPLY);
            assertThat(notification.getReceiver()).isEqualTo(1L);
            assertThat(notification).isInstanceOf(LetterNotification.class);
            assertThat(((LetterNotification) notification).getLetterId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("키워드 편지 답장 알림을 생성한다.")
        public void replyKeywordLetterNotificationTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("KEYWORD_REPLY", 1L, 1L, null);

            // WHEN
            Notification notification = Notification.create(NotificationType.from(request.notificationType()),
                    request.receiver(), request.letterId(), request.label());

            // THEN
            assertThat(notification.getType()).isEqualTo(NotificationType.KEYWORD_REPLY);
            assertThat(notification.getReceiver()).isEqualTo(1L);
            assertThat(notification).isInstanceOf(LetterNotification.class);
            assertThat(((LetterNotification) notification).getLetterId()).isEqualTo(1L);
        }


        @Test
        @DisplayName("새 편지 생성 시, 편지 ID가 없으면 예외를 발생시킨다.")
        public void replyLetterNoLetterIdTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("KEYWORD_REPLY", 1L, null, null);

            // WHEN
            assertThatThrownBy(
                    () -> Notification.create(NotificationType.from(request.notificationType()), request.receiver(),
                            request.letterId(), request.label()))
                    .isInstanceOf(NoLetterIdException.class);
        }

        @Test
        @DisplayName("유저 경고 알림을 생성한다.")
        public void warningNotificationTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("WARNING", 1L, null, null);

            // WHEN
            Notification notification = Notification.create(NotificationType.from(request.notificationType()),
                    request.receiver(), request.letterId(), request.label());

            // THEN
            assertThat(notification.getType()).isEqualTo(NotificationType.WARNING);
            assertThat(notification.getReceiver()).isEqualTo(1L);
            assertThat(notification).isNotInstanceOf(LetterNotification.class);
        }

        @Test
        @DisplayName("유저 정지 알림을 생성한다.")
        public void banNotificationTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("BAN", 1L, null, null);

            // WHEN
            Notification notification = Notification.create(NotificationType.from(request.notificationType()),
                    request.receiver(), request.letterId(), request.label());

            // THEN
            assertThat(notification.getType()).isEqualTo(NotificationType.BAN);
            assertThat(notification.getReceiver()).isEqualTo(1L);
            assertThat(notification).isNotInstanceOf(LetterNotification.class);
        }
    }

    @Nested
    @DisplayName("알림 읽음")
    class NotificationRead {
        @Test
        @DisplayName("알림을 읽는다면, 읽음 표시를 한다.")
        public void readNotification() {
            // GIVEN
            Notification notification = Notification.create(NotificationType.NEW_LETTER, 1L, 1L, "label");

            // WHEN
            Notification read = notification.read();

            // THEN
            assertThat(read.getIsRead()).isTrue();
        }
    }
}
