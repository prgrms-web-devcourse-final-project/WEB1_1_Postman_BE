package postman.bottler.notification.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.Notifications;
import postman.bottler.notification.domain.PushMessages;
import postman.bottler.notification.domain.Subscriptions;
import postman.bottler.notification.dto.response.NotificationResponseDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PushNotificationProvider pushNotificationProvider;

    @Transactional
    public NotificationResponseDTO sendNotification(String type, Long userId, Long letterId) {
        Notification notification = Notification.create(type, userId, letterId);
        Subscriptions subscriptions = subscriptionRepository.findByUserId(userId);
        NotificationResponseDTO result = NotificationResponseDTO.from(notificationRepository.save(notification));
        if (subscriptions.isPushEnabled()) {
            sendPushMessagesToUser(subscriptions, notification);
        }
        return result;
    }

    private void sendPushMessagesToUser(Subscriptions subscriptions, Notification notification) {
        PushMessages pushMessages = subscriptions.makeMessages(notification.getType());
        pushNotificationProvider.pushAll(pushMessages);
    }

    @Transactional
    public List<NotificationResponseDTO> getUserNotifications(Long userId) {
        Notifications notifications = notificationRepository.findByReceiver(userId);
        notifications.orderByCreatedAt();
        List<NotificationResponseDTO> result = notifications.createDTO();
        Notifications changed = notifications.markAsRead();
        notificationRepository.updateNotifications(changed);
        return result;
    }
}
