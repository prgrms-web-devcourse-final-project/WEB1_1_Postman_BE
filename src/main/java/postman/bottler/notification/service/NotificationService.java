package postman.bottler.notification.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.PushMessage;
import postman.bottler.notification.domain.Subscription;
import postman.bottler.notification.dto.response.NotificationResponseDTO;
import postman.bottler.notification.dto.response.SubscriptionResponseDTO;

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
        List<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);
        subscriptions.forEach(subscription -> {
            PushMessage pushMessage = subscription.makeMessage(notification.getType());
            pushNotificationProvider.push(pushMessage);
        });
        return NotificationResponseDTO.from(notificationRepository.save(notification));
    }

    @Transactional
    public List<NotificationResponseDTO> getUserNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByReceiver(userId);
        List<NotificationResponseDTO> result = notifications.stream()
                .map(NotificationResponseDTO::from)
                .toList();
        notifications.stream()
                .filter(notification -> !notification.getIsRead())
                .forEach(Notification::read);
        notificationRepository.updateNotifications(notifications);
        return result;
    }
}
