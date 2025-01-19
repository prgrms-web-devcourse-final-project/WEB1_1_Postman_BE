package postman.bottler.notification.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static postman.bottler.notification.domain.NotificationType.BAN;
import static postman.bottler.notification.domain.NotificationType.KEYWORD_REPLY;
import static postman.bottler.notification.domain.NotificationType.MAP_REPLY;
import static postman.bottler.notification.domain.NotificationType.NEW_LETTER;
import static postman.bottler.notification.domain.NotificationType.TARGET_LETTER;
import static postman.bottler.notification.domain.NotificationType.WARNING;
import static postman.bottler.notification.domain.NotificationType.from;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import postman.bottler.notification.application.dto.request.NotificationRequestDTO;
import postman.bottler.notification.application.dto.request.RecommendNotificationRequestDTO;
import postman.bottler.notification.application.dto.response.UnreadNotificationResponseDTO;
import postman.bottler.notification.application.dto.response.NotificationResponseDTO;
import postman.bottler.notification.application.repository.NotificationRepository;
import postman.bottler.notification.application.repository.SubscriptionRepository;
import postman.bottler.notification.application.service.NotificationService;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.NotificationType;
import postman.bottler.notification.domain.Notifications;
import postman.bottler.notification.domain.Subscription;
import postman.bottler.notification.domain.Subscriptions;
import postman.bottler.notification.exception.NoLetterIdException;

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
            Notification notification = Notification.create(NEW_LETTER, 1L, 1L, "label");

            given(notificationRepository.save(any())).willReturn(notification);
            given(subscriptionRepository.findByUserId(1L))
                    .willReturn(Subscriptions.from(List.of(Subscription.create(1L, "token"))));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(NEW_LETTER, 1L, 1L, "label");

            // THEN
            assertThat(response)
                    .extracting("type", "receiver", "letterId", "label")
                    .containsExactlyInAnyOrder(NEW_LETTER, 1L, 1L, "label");
        }

        @Test
        @DisplayName("타겟 편지 알림을 보낸다.")
        public void sendTargetLetterNotificationTest() {
            // GIVEN
            Notification notification = Notification.create(TARGET_LETTER, 1L, 1L, "label");

            given(notificationRepository.save(any())).willReturn(notification);
            given(subscriptionRepository.findByUserId(1L))
                    .willReturn(Subscriptions.from(List.of(Subscription.create(1L, "token"))));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(TARGET_LETTER, 1L, 1L, "label");

            // THEN
            assertThat(response)
                    .extracting("type", "receiver", "letterId", "label")
                    .containsExactlyInAnyOrder(TARGET_LETTER, 1L, 1L, "label");
        }

        @Test
        @DisplayName("지도 답장 편지 알림을 보낸다.")
        public void sendMapReplyLetterNotificationTest() {
            // GIVEN
            Notification notification = Notification.create(MAP_REPLY, 1L, 1L, "label");

            given(notificationRepository.save(any())).willReturn(notification);
            given(subscriptionRepository.findByUserId(1L)).willReturn(
                    Subscriptions.from(List.of(Subscription.create(1L, "token"))));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(MAP_REPLY, 1L, 1L, "label");

            // THEN
            assertThat(response)
                    .extracting("type", "receiver", "letterId", "label")
                    .containsExactlyInAnyOrder(MAP_REPLY, 1L, 1L, "label");
        }

        @Test
        @DisplayName("키워드 답장 편지 알림을 보낸다.")
        public void sendKeywordReplyLetterNotificationTest() {
            // GIVEN
            Notification notification = Notification.create(KEYWORD_REPLY, 1L, 1L, "label");

            given(notificationRepository.save(any())).willReturn(notification);
            given(subscriptionRepository.findByUserId(1L))
                    .willReturn(Subscriptions.from(List.of(Subscription.create(1L, "token"))));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(KEYWORD_REPLY, 1L, 1L, "label");

            // THEN
            assertThat(response)
                    .extracting("type", "receiver", "letterId", "label")
                    .containsExactlyInAnyOrder(KEYWORD_REPLY, 1L, 1L, "label");
        }

        @DisplayName("편지 관련 알림에 편지 ID가 없는 경우, 예외가 발생한다.")
        @ParameterizedTest
        @CsvSource({"NEW_LETTER", "TARGET_LETTER", "MAP_REPLY", "KEYWORD_REPLY"})
        void sendNotificationWithoutLetterId(NotificationType notificationType) {
            // when then
            assertThatThrownBy(() -> notificationService.sendNotification(notificationType, 1L, null, "label"))
                    .isInstanceOf(NoLetterIdException.class);
        }

        @Test
        @DisplayName("경고 알림을 보낸다.")
        public void sendWarningNotificationTest() {
            // GIVEN
            Notification notification = Notification.create(WARNING, 1L, null, null);

            given(notificationRepository.save(any())).willReturn(notification);
            given(subscriptionRepository.findByUserId(1L))
                    .willReturn(Subscriptions.from(List.of(Subscription.create(1L, "token"))));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(WARNING, 1L, null, null);

            // THEN
            assertThat(response)
                    .extracting("type", "receiver", "letterId", "label")
                    .containsExactlyInAnyOrder(WARNING, 1L, null, null);
        }

        @Test
        @DisplayName("정지 알림을 보낸다.")
        public void sendBanNotificationTest() {
            // GIVEN
            Notification notification = Notification.create(BAN, 1L, null, null);

            given(notificationRepository.save(any())).willReturn(notification);
            given(subscriptionRepository.findByUserId(1L))
                    .willReturn(Subscriptions.from(List.of(Subscription.create(1L, "token"))));

            // WHEN
            NotificationResponseDTO response = notificationService.sendNotification(BAN, 1L, null, null);

            // THEN
            assertThat(response)
                    .extracting("type", "receiver", "letterId", "label")
                    .containsExactlyInAnyOrder(BAN, 1L, null, null);
        }

        @DisplayName("편지 관련 알림이 아닐 경우, 편지 ID와 라벨은 null로 저장된다.")
        @ParameterizedTest
        @CsvSource({"WARNING", "BAN"})
        void sendNonLetterNotificationWithLabelAndLetterId(NotificationType notificationType) {
            // given
            Notification notification = Notification.create(notificationType, 1L, 1L, "label");

            given(notificationRepository.save(any())).willReturn(notification);
            given(subscriptionRepository.findByUserId(1L))
                    .willReturn(Subscriptions.from(List.of(Subscription.create(1L, "token"))));

            // when
            NotificationResponseDTO response = notificationService.sendNotification(BAN, 1L, 1L, "label");

            // then
            assertThat(response)
                    .extracting("letterId", "label")
                    .containsExactlyInAnyOrder(null, null);
        }

        @Test
        @DisplayName("알림 보낼 기기가 없다면, 보내지 않는다.")
        public void notSendPushNotification() {
            // GIVEN
            NotificationRequestDTO request = new NotificationRequestDTO("BAN", 1L, 1L, "label");
            Notification notification = Notification.create(from(request.notificationType()),
                    request.receiver(), request.letterId(), request.label());

            given(notificationRepository.save(any())).willReturn(notification);
            given(subscriptionRepository.findByUserId(1L))
                    .willReturn(Subscriptions.from(List.of()));

            // WHEN
            notificationService.sendNotification(from(request.notificationType()), request.receiver(),
                    request.letterId(), request.label());

            // THEN
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
            ArrayList<Notification> notificationList = new ArrayList<>();
            notificationList.add(Notification.create(NEW_LETTER, 1L, 1L, "label"));
            notificationList.add(Notification.create(TARGET_LETTER, 1L, 1L, "label"));
            notificationList.add(Notification.create(MAP_REPLY, 1L, 1L, "label"));
            Notifications notifications = Notifications.from(notificationList);

            given(notificationRepository.findByReceiver(any())).willReturn(notifications);

            // WHEN
            List<NotificationResponseDTO> notReadResponse = notificationService.getUserNotifications(1L);

            // THEN
            assertThat(notReadResponse).hasSize(3)
                    .extracting("receiver", "type")
                    .containsExactlyInAnyOrder(
                            tuple(1L, NEW_LETTER),
                            tuple(1L, TARGET_LETTER),
                            tuple(1L, MAP_REPLY));
        }
    }

    @Nested
    @DisplayName("편지 추천 알림")
    class Recommendation {

        @DisplayName("추천된 편지를 모든 유저에게 알림을 전송한다.")
        @Test
        void sendKeywordNotifications() {
            // given
            List<RecommendNotificationRequestDTO> recommends = List.of(
                    new RecommendNotificationRequestDTO(1L, 1L, "label"),
                    new RecommendNotificationRequestDTO(2L, 1L, "label"));

            given(subscriptionRepository.findAll()).willReturn(
                    Subscriptions.from(List.of(Subscription.create(1L, "token"), Subscription.create(2L, "token"))));

            // when
            notificationService.sendKeywordNotifications(recommends);

            // then
            verify(notificationRepository, times(2)).save(any());
            verify(pushNotificationProvider, times(1)).pushAll(any());
        }
    }

    @DisplayName("사용자의 읽지 않은 알림의 개수를 반환한다.")
    @Test
    void getUnreadNotificationCount() {
        // given
        given(notificationRepository.findByReceiver(any()))
                .willReturn(Notifications.from(Arrays.asList(
                        Notification.of(UUID.randomUUID(), WARNING, 1L, null, LocalDateTime.now(), true, null),
                        Notification.of(UUID.randomUUID(), BAN, 1L, null, LocalDateTime.now(), false, null))));

        // when
        UnreadNotificationResponseDTO result = notificationService.getUnreadNotificationCount(1L);

        // then
        assertThat(result.count()).isEqualTo(1L);
    }
}
