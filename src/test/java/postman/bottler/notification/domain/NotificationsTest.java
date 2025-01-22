package postman.bottler.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static postman.bottler.notification.domain.NotificationType.WARNING;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NotificationsTest {

    @DisplayName("알림들을 최신순으로 정렬한다.")
    @Test
    void orderByCreatedAt() {
        // given
        ArrayList<Notification> notificationList = new ArrayList<>();
        Notification notification1 = Notification.create(WARNING, 1L, null, null);
        Notification notification2 = Notification.create(WARNING, 1L, null, null);
        Notification notification3 = Notification.create(WARNING, 1L, null, null);
        notificationList.add(notification1);
        notificationList.add(notification2);
        notificationList.add(notification3);
        Notifications notifications = Notifications.from(notificationList);

        // when
        notifications.orderByCreatedAt();

        // then
        Comparator<Notification> latestOrder = Comparator.comparing(Notification::getCreatedAt).reversed();
        assertThat(notifications.getNotifications()).isSortedAccordingTo(latestOrder);
    }

    @DisplayName("알림들 읽음 처리 시, 읽음 처리된 알림만 반환한다.")
    @Test
    void markAsRead() {
        // given
        ArrayList<Notification> notificationList = new ArrayList<>();
        Notification notification1 = Notification.of(UUID.randomUUID(), WARNING, 1L, null, LocalDateTime.now(), true,
                "label");
        Notification notification2 = Notification.create(WARNING, 1L, null, null);
        Notification notification3 = Notification.create(WARNING, 1L, null, null);
        notificationList.add(notification1);
        notificationList.add(notification2);
        notificationList.add(notification3);
        Notifications notifications = Notifications.from(notificationList);

        // when
        Notifications markAsRead = notifications.markAsRead();

        // then
        assertThat(markAsRead.getNotifications()).hasSize(2)
                .extracting("isRead")
                .contains(true);
    }

    @DisplayName("읽지 않은 알림의 개수를 조회한다.")
    @Test
    void getUnreadCount() {
        // given
        Notification n1 = Notification.of(UUID.randomUUID(), WARNING, 1L, null, LocalDateTime.now(), false, null);
        Notification n2 = Notification.of(UUID.randomUUID(), WARNING, 1L, null, LocalDateTime.now(), false, null);
        Notification n3 = Notification.of(UUID.randomUUID(), WARNING, 1L, null, LocalDateTime.now(), true, null);
        Notifications notifications = Notifications.from(Arrays.asList(n1, n2, n3));

        // when
        long count = notifications.getUnreadCount();

        // then
        assertThat(count).isEqualTo(2);
    }
}
