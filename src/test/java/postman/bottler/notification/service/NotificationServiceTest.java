package postman.bottler.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.NotificationType;
import postman.bottler.notification.domain.Subscription;
import postman.bottler.notification.domain.Subscriptions;
import postman.bottler.notification.dto.request.NotificationRequestDTO;
import postman.bottler.notification.dto.response.NotificationResponseDTO;

@DisplayName("알림 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @InjectMocks
    private NotificationService notificationService;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private PushNotificationProvider pushNotificationProvider;

    @Nested
    @DisplayName("알림 생성")
    class CreateNotification {
        @Test
        @DisplayName("새 편지 알림을 보낸다.")
        public void sendNewLetterNotificationTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("NEW_LETTER", 1L, 1L);
            Notification notification = Notification.create(request.notificationType(), request.receiver(),
                    request.letterId());
            when(notificationRepository.save(any())).thenReturn(notification);
            when(subscriptionRepository.findByUserId(1L))
                    .thenReturn(Subscriptions.from(List.of(Subscription.create(1L, "token"))));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(
                    request.notificationType(),
                    request.receiver(),
                    request.letterId());

            // THEN
            assertThat(response.receiver()).isEqualTo(1L);
            assertThat(response.type()).isEqualTo(NotificationType.NEW_LETTER);
            assertThat(response.letterId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("타겟 편지 알림을 보낸다.")
        public void sendTargetLetterNotificationTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("TARGET_LETTER", 1L, 1L);
            Notification notification = Notification.create(request.notificationType(), request.receiver(),
                    request.letterId());
            when(notificationRepository.save(any())).thenReturn(notification);
            when(subscriptionRepository.findByUserId(1L))
                    .thenReturn(Subscriptions.from(List.of(Subscription.create(1L, "token"))));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(
                    request.notificationType(),
                    request.receiver(),
                    request.letterId());

            // THEN
            assertThat(response.receiver()).isEqualTo(1L);
            assertThat(response.type()).isEqualTo(NotificationType.TARGET_LETTER);
            assertThat(response.letterId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("답장 편지 알림을 보낸다.")
        public void sendReplyLetterNotificationTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("REPLY_LETTER", 1L, 1L);
            Notification notification = Notification.create(request.notificationType(), request.receiver(),
                    request.letterId());
            when(notificationRepository.save(any())).thenReturn(notification);
            when(subscriptionRepository.findByUserId(1L))
                    .thenReturn(Subscriptions.from(List.of(Subscription.create(1L, "token"))));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(
                    request.notificationType(),
                    request.receiver(),
                    request.letterId());

            // THEN
            assertThat(response.receiver()).isEqualTo(1L);
            assertThat(response.type()).isEqualTo(NotificationType.REPLY_LETTER);
            assertThat(response.letterId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("경고 알림을 보낸다.")
        public void sendWarningNotificationTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("WARNING", 1L, 1L);
            Notification notification = Notification.create(request.notificationType(), request.receiver(),
                    request.letterId());
            when(notificationRepository.save(any())).thenReturn(notification);
            when(subscriptionRepository.findByUserId(1L))
                    .thenReturn(Subscriptions.from(List.of(Subscription.create(1L, "token"))));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(
                    request.notificationType(),
                    request.receiver(),
                    request.letterId());

            // THEN
            assertThat(response.receiver()).isEqualTo(1L);
            assertThat(response.type()).isEqualTo(NotificationType.WARNING);
            assertThat(response.letterId()).isNull();
        }

        @Test
        @DisplayName("경고 알림을 보낸다.")
        public void sendBanNotificationTest() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("BAN", 1L, 1L);
            Notification notification = Notification.create(request.notificationType(), request.receiver(),
                    request.letterId());
            when(notificationRepository.save(any())).thenReturn(notification);
            when(subscriptionRepository.findByUserId(1L))
                    .thenReturn(Subscriptions.from(List.of(Subscription.create(1L, "token"))));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(
                    request.notificationType(),
                    request.receiver(),
                    request.letterId());

            // THEN
            assertThat(response.receiver()).isEqualTo(1L);
            assertThat(response.type()).isEqualTo(NotificationType.BAN);
            assertThat(response.letterId()).isNull();
        }

        @Test
        @DisplayName("알림 보낼 기기가 없다면, 보내지 않는다.")
        public void notSendPushNotification() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("BAN", 1L, 1L);
            Notification notification = Notification.create(request.notificationType(), request.receiver(),
                    request.letterId());
            when(notificationRepository.save(any())).thenReturn(notification);
            when(subscriptionRepository.findByUserId(1L))
                    .thenReturn(Subscriptions.from(List.of()));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(
                    request.notificationType(),
                    request.receiver(),
                    request.letterId());

            // THEN
            assertThat(response.receiver()).isEqualTo(1L);
            assertThat(response.type()).isEqualTo(NotificationType.BAN);
            assertThat(response.letterId()).isNull();
            verify(pushNotificationProvider, times(0)).pushAll(any());
        }
    }

    @Nested
    @DisplayName("알림 조회")
    class GetNotification {
        @Test
        @DisplayName("사용자의 알림을 조회한다.")
        public void getNotifications() {
            // GIVEN
            List<Notification> notReadNotification = List.of(Notification.create("NEW_LETTER", 1L, 1L),
                    Notification.create("TARGET_LETTER", 1L, 1L), Notification.create("REPLY_LETTER", 1L, 1L),
                    Notification.create("WARNING", 1L, null), Notification.create("BAN", 1L, null));

            when(notificationRepository.findByReceiver(any())).thenReturn(notReadNotification)
                    .thenAnswer(invocation -> {
                        notReadNotification.forEach(Notification::read);
                        return notReadNotification;
                    });

            // WHEN
            List<NotificationResponseDTO> notReadResponse = notificationService.getUserNotifications(1L);
            List<NotificationResponseDTO> readResponse = notificationService.getUserNotifications(1L);

            // THEN
            assertThat(notReadResponse.stream().filter(r -> !r.isRead()).count()).isEqualTo(5L);
            assertThat(readResponse.stream().filter(NotificationResponseDTO::isRead).count()).isEqualTo(5L);
        }
    }
}
