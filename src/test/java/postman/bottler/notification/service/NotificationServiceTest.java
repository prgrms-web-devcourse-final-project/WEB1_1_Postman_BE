package postman.bottler.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.NotificationType;
import postman.bottler.notification.dto.request.NotificationRequestDTO;
import postman.bottler.notification.dto.response.NotificationResponseDTO;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Nested
    @DisplayName("알림 생성")
    class CreateNotification {
        @Test
        @DisplayName("새 편지 알림을 보낸다.")
        public void sendNewLetterNotificationTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("NEW_LETTER", 1L, 1L);
            when(notificationRepository.save(any()))
                    .thenReturn(Notification.of(request.notificationType(), request.receiver(), request.letterId()));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(
                    request.notificationType(),
                    request.receiver(),
                    request.letterId());

            // THEN
            assertThat(response.receiver()).isEqualTo(1L);
            assertThat(response.type()).isEqualTo(NotificationType.NEW_LETTER);
        }

        @Test
        @DisplayName("타겟 편지 알림을 보낸다.")
        public void sendTargetLetterNotificationTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("TARGET_LETTER", 1L, 1L);
            when(notificationRepository.save(any()))
                    .thenReturn(Notification.of(request.notificationType(), request.receiver(), request.letterId()));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(
                    request.notificationType(),
                    request.receiver(),
                    request.letterId());

            // THEN
            assertThat(response.receiver()).isEqualTo(1L);
            assertThat(response.type()).isEqualTo(NotificationType.TARGET_LETTER);
        }

        @Test
        @DisplayName("답장 편지 알림을 보낸다.")
        public void sendReplyLetterNotificationTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("REPLY_LETTER", 1L, 1L);
            when(notificationRepository.save(any()))
                    .thenReturn(Notification.of(request.notificationType(), request.receiver(), request.letterId()));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(
                    request.notificationType(),
                    request.receiver(),
                    request.letterId());

            // THEN
            // THEN
            assertThat(response.receiver()).isEqualTo(1L);
            assertThat(response.type()).isEqualTo(NotificationType.REPLY_LETTER);
        }

        @Test
        @DisplayName("경고 알림을 보낸다.")
        public void sendWarningNotificationTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("WARNING", 1L, 1L);
            when(notificationRepository.save(any()))
                    .thenReturn(Notification.of(request.notificationType(), request.receiver(), request.letterId()));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(
                    request.notificationType(),
                    request.receiver(),
                    request.letterId());

            // THEN
            assertThat(response.receiver()).isEqualTo(1L);
            assertThat(response.type()).isEqualTo(NotificationType.WARNING);
        }

        @Test
        @DisplayName("경고 알림을 보낸다.")
        public void sendBanNotificationTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("BAN", 1L, 1L);
            when(notificationRepository.save(any()))
                    .thenReturn(Notification.of(request.notificationType(), request.receiver(), request.letterId()));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(
                    request.notificationType(),
                    request.receiver(),
                    request.letterId());

            // THEN
            assertThat(response.receiver()).isEqualTo(1L);
            assertThat(response.type()).isEqualTo(NotificationType.BAN);
        }
    }
}
